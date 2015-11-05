
public class Block 
{
	public static String TEXT = "■";
	
	int position;
	boolean isActive = true;
	
	public Block(int position)
	{
		this.position = position;
	}
	public void moveNextPos()
	{
		if (this.position % 20 == 0) //If the blocks is on the farthest side
		{
			isActive = false; //When block is off screen and should not be referenced anymore
			return;
		}
		this.position -= 1;
	}
}
