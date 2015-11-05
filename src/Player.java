
public class Player 
{
	int position;
	boolean isComingDown = false;  //Used to see if program should move player up
	boolean firstJump = false; //Used so two spaces aren't skipped when updating
	
	public Player(int position)
	{
		this.position = position;
	}
	
	public void Jump()
	{
		if (this.position != 162)  // If player is not on the floor
		{
			System.out.println("Did not jump!");
			return;
		}
		this.position -= 20;
		firstJump = true;
	}
}
