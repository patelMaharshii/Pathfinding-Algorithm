import java.io.FileNotFoundException;
import java.io.IOException;

public class FrogPath {

	private Pond pond;
	
	public FrogPath(String filename) {
		
		try {
			this.pond = new Pond(filename);
		} catch (InvalidMapException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	public Hexagon findBest(Hexagon currCell) {
		
		ArrayUniquePriorityQueue<Hexagon> array = new ArrayUniquePriorityQueue<Hexagon>();
		
		for(int i = 0; i < 6; i++) {
			Hexagon add = currCell.getNeighbour(i);
			double cellPriority = this.getCellPriority(add, i);
			
			if(cellPriority != 15) {
				array.add(add, cellPriority);
			}
		}
		
		if(currCell.isLilyPadCell()) {
			
			int counter = 6;
			
			for(int i = 0; i < 6; i++) {
				
				Hexagon neighbour = currCell.getNeighbour(i);
				
				for(int j = 0; j < 6; j++) {
					
					Hexagon add = neighbour.getNeighbour(j);
					double cellPriority = this.getCellPriority(add, counter);
					
					if(!array.contains(add) && add.getID() != currCell.getID() && cellPriority != 15 && !add.isMarkedInStack()) {
						array.add(add, cellPriority);
						counter++;
					}
				}
			}
			
		}
		
		if(array.isEmpty()) {
			return null;
		} else {
			return array.peek();
		}
		
	}
	
	private double getCellPriority(Hexagon cell, int index) {
		
		double priority = 0.0;
		
		if(cell.isAlligator() || cell.isMudCell() || (this.alligatorNear(cell) && !cell.isReedsCell())) {
			return 15;
		} else if(this.alligatorNear(cell) && cell.isReedsCell()) {
			priority += 10.0;
		} else if(cell.isWaterCell()) {
			priority += 6.0;
		} else if(cell.isReedsCell()) {
			priority += 5.0;
		} else if(cell.isLilyPadCell()) {
			priority += 4.0;
		} else if(cell.isEnd()) {
			priority += 3.0;
		} else if(cell instanceof FoodHexagon) {
			int flies = ((FoodHexagon) cell).getNumFlies();
			switch(flies) {
				case 1:
					priority += 2.0;
				case 2:
					priority += 1.0;
				case 3:
					priority += 0.0;
			}
		}
		
		if(index >= 6) {
			switch(index) {
				case 8:
				case 11:
				case 14:
				case 17:
					priority += 0.5;
				default:
					priority += 1.0;
				
			}
		}
		
		return priority;
	}
	
	private boolean alligatorNear(Hexagon cell) {
		
		for(int i = 0; i < 6; i++) {
			if(cell.getNeighbour(i).isAlligator()) {
				return true;
			}
		}
		return false;
		
	}
	
	public String findPath() {
		
		ArrayStack<Hexagon> S = new ArrayStack<Hexagon>();
		S.push(this.pond.getStart());
		this.pond.getStart().markInStack();
		int fliesEaten = 0;
		String path = "";
		
		while(!S.isEmpty()) {
			
			Hexagon curr = S.peek();
			path += curr.toString() + " ";
			
			if(curr.isEnd()) {
				break;
			}
			
			if(curr instanceof FoodHexagon) {
				
				fliesEaten = fliesEaten + ((FoodHexagon) curr).getNumFlies();
				((FoodHexagon) curr).removeFlies();
				
			}
			
			Hexagon next = findBest(curr);
			
			if(next == null) {
				S.pop();
				curr.markOutStack();
			} else {
				S.push(next);
				next.markInStack();
			}
			
		}
		
		if(S.isEmpty()) {
			return "No solution";
		} else {
			return path + "ate " + fliesEaten + " flies";
		}
		
	}
	
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			 System.out.println("No map file specified in the arguments");
			 return;
		}
		 FrogPath fp = new FrogPath("pond1.txt");
		 Hexagon.TIME_DELAY = 500; // Change this time delay as desired.
		 String result = fp.findPath();
		 System.out.println(result);
		
	}
	
}
