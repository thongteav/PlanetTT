import java.util.ArrayList;
import java.util.Random;

import com.jogamp.opengl.GL2;

public class Tree {
	private ArrayList<Branch> branches;
	private ArrayList<Leaf> leaves;
	private float length;
	private double angle;
	private static Random rand = new Random(System.currentTimeMillis());
	
	public Tree(float x, float y, float length, int level, double angle) {
		this.angle = angle;
		this.length = length;
		branches = new ArrayList<>();
		double rad = Math.toRadians(angle);
		Branch stem = new Branch(x, y, (float) (x + Math.cos(rad) * length), (float) (y + Math.sin(rad) * length), angle, level, 1);
		branches.add(stem);
		
		//construct the tree of the level specified
		for(int i = 0; i < level; ++i) {
			for (int j = branches.size() - 1; j >= 0; --j) {//looping through the branches after each new level
				Branch branch = branches.get(j);
				if(!branch.isFinished()) {//only create the new branch if it hasn't been extended
					//create the new branches shorter than the current branch at 45 degrees
					this.createBranch(branches.get(j), 45, (float) (0.89 / (i+1)), i+1); 
				}
			}
		}
		
		leaves = new ArrayList<>();
		this.createLeaves();
	}
	
	/**
	 * Create the leaves at the leaf branches
	 */
	public void createLeaves() {
		for(Branch b: branches) {
			if(!b.isFinished()) {
				leaves.add(new Leaf(b.getxTail(), b.getyTail(), 0.01, this.angle));
			}
		}
	}
	
	/**
	 * Creates two branches from the specified branch at a random angle.
	 * 
	 * @param branch the branch to be extended
	 * @param deg the degree to be growing at
	 * @param shrinkFactor the scaling of the length compared to the specified branch with < 1 being shorter
	 * @param level the level of the branch of the tree
	 */
	public void createBranch(Branch branch, int deg, float shrinkFactor, int level) {
		double angleRight = branch.getAngle() - deg + (rand.nextDouble() * 100 - 40);
		double angleLeft = branch.getAngle() + deg + (rand.nextDouble() * 80 - 45);
		double radRight = Math.toRadians(angleRight);
		double radLeft = Math.toRadians(angleLeft);
		float xTailRight = (float) (branch.getxTail() + this.getRandomLength() * Math.cos(radRight) * shrinkFactor);
		float yTailRight = (float) (branch.getyTail() + this.getRandomLength() * Math.sin(radRight) * shrinkFactor);
		float xTailLeft = (float) (branch.getxTail() + this.getRandomLength() * Math.cos(radLeft) * shrinkFactor);
		float yTailLeft = (float) (branch.getyTail() + this.getRandomLength() * Math.sin(radLeft) * shrinkFactor);
		//Branch(xHead, yHead, xTail, yTail, angle, thickness, level)
		Branch rightB = new Branch(branch.getxTail(), branch.getyTail(), xTailRight, yTailRight, angleRight, Math.max(2f, branch.getThickness() - level), level);
		Branch leftB = new Branch(branch.getxTail(), branch.getyTail(), xTailLeft, yTailLeft, angleLeft, Math.max(2f, branch.getThickness() - level), level);
		branches.add(rightB);
		branches.add(leftB);
		branch.setFinished(true);//flag the branch already created extra branches
	}
	
	public double getRandomLength() {
		return rand.nextDouble() * 0.1 + this.length;
	}
	
	public void draw(GL2 gl) {
		for(Branch b: branches) {
			b.draw(gl);
		}
		
		for(Leaf l: leaves) {
			l.draw(gl);
		}
	}
	
	public void update(double time) {
		for(Branch b: branches) {
			b.update(time);
		}
		
		for(Leaf l: leaves) {
			l.update(time);
		}
	}
	
	public void setWindOn(boolean windOn) {
		for(Leaf l: leaves) {
			l.setWindOn(windOn);
		}
	}
}
