
public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T> {

	private T[] queue;
	private double[] priority;
	private int count;
	
	@SuppressWarnings("unchecked")
	public ArrayUniquePriorityQueue() {
		
		this.queue = (T[]) new Object[10];
		this.priority = new double[10];
		this.count = 0; 
		
	}
	
	@Override
	public void add(T data, double prio) {
		
		//If the data is already within the list, then exit method
		if(this.contains(data)) return;
		
		// If the array is full, expand capacity by 5
		if(this.count == this.queue.length) expandCapacity();
		
		if(this.count == 0) {
			
			this.queue[0] = data;
			this.priority[0] = prio;
			this.count++;
			
		} else if(this.priority[this.count - 1] <= prio) {
			
			this.queue[this.count] = data;
			this.priority[this.count] = prio;
			this.count++;
			
		} else {
			
			int i;
			int index = 0;
			
			for(i = 0; i < this.count; i++) {
				
				if(prio < this.priority[i]) {
					index = i;
					break;
				}
				
			}
						
			for(i = this.count - 1; i >= index; i--) {
								
				this.queue[i+1] = this.queue[i];
				this.priority[i+1] = this.priority[i];
				
			}
			
			this.queue[index] = data;
			this.priority[index] = prio;
			this.count++;
			
		}
		
	}

	@Override
	public boolean contains(T data) {
		
		int i;
		
		for(i = 0; i < this.count; i++) {
			
			if(this.queue[i] != null && this.queue[i].equals(data)) {
				return true;
			}
			
		}
		
		return false;
		
	}

	@Override
	public T peek() throws CollectionException {
		
		if(this.count == 0) {
			throw new CollectionException("PQ is empty");
		} else {
			
			int lowestPriority = 0;
			int i;
			
			for(i = 1; i < this.count; i++) {
				
				if(this.priority[i] < this.priority[lowestPriority]) {
					lowestPriority = i;
				}
				
			}
			
			return this.queue[lowestPriority];
			
		}
		
	}

	@Override
	public T removeMin() throws CollectionException {
		
		if (isEmpty()) throw new CollectionException("PQ is empty");

		T min = this.peek();
		this.remove(min);
        
        return min;
		
	}

	@Override
	public void updatePriority(T data, double newPrio) throws CollectionException {
		
		if (!contains(data)) throw new CollectionException("Item not found in PQ");
		this.remove(data);
		this.add(data, newPrio);
		
	}

	@Override
	public boolean isEmpty() {
		return this.count == 0;
	}

	@Override
	public int size() {
		
		int size = 0;
		
		for(T i: this.queue) {
			
			if(i == null) {
				break;
			}
			size++;
			
		}
		
		return size;
		
	}
	
	public int getLength() {
		return this.queue.length;
	}
	
	public String toString() {
		
		if (isEmpty()) {
			
			return "The PQ is empty";
			
		} else {
			
			String returning = "";
			int i;
			
			for(i = 0; i < this.count; i++) {
				
				returning += this.queue[i];
				returning += " [" + this.priority[i] + "]";
				
				if(i != this.count - 1) {
					returning += ", ";
				}
				
			}
			
			return returning;
			
		}
		
	}
	
	private void expandCapacity() {

		int arrLen = this.queue.length; // Array current length
		
		// Make new generic arrays and increase their length by 5
		T[] expandedQueue = (T[]) new Object[arrLen + 5];
        double[] expandedPriority = new double[arrLen + 5];

        // Reassign values from old arrays into new ones
        int i;
        for (i = 0; i < arrLen; i++) {
        	expandedQueue[i] = this.queue[i];
        	expandedPriority[i] = this.priority[i];
        }
        
        // Reassign instance variables with their new arrays
        this.queue = expandedQueue;
        this.priority = expandedPriority;
    }
	
	private void remove(T data) {
        
		int dataIndex = -1;
        
		// Find the index at which the data we want to remove exists
		int i;
		for (i = 0; i < this.count; i++) {
			
            if (this.queue[i].equals(data)) {
            	
            	dataIndex = i;
                break;
                
            }
            
        }
			
		// If the data exists, start from the dataIndex and override with the next one until done
        if (dataIndex != -1) {
        	
            for (i = dataIndex; i < this.count - 1; i++) {
            	
                this.queue[i] = queue[i + 1];
                this.priority[i] = priority[i + 1];
                
            }
            
            count--;
        }
			
        
    }
	
}
