import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends JFrame implements KeyListener
{
	Random randomNumber = new Random();
	
	int timeUntilNextGenerate = 0; //Used for creating a gap between blocks
	
	Player player = new Player(162);
	
	Block[] blocks = new Block[200];
	
	JPanel Ground = new JPanel();
	JPanel GameWindow = new JPanel();
	
	JLabel[] positions = new JLabel[181];
	
	
	public MainWindow()
	{
		//Adds the timer to call Update()
		Timer gameTimer = new Timer();
		gameTimer.scheduleAtFixedRate(new TimerTask()
				{
					@Override
					public void run()
					{
						
						UpdateGame();	
						GenerateBlocks();
					}
				}, 200, 200);  //Creates a new timer that calls Update() every half second
		
		this.addKeyListener(this);
		this.setLayout(new BorderLayout()); 
				
		initGame();
		
		this.add(GameWindow, BorderLayout.NORTH);
		this.add(Ground, BorderLayout.SOUTH);
		
		//this.setSize(800, 400);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.setVisible(true);
	}
	
	public void initGame()
	{
		//Initializes the game window
		GameWindow.setLayout(new GridLayout(0, 20)); //Set row to 0 to prevent columns from filling
		for (int i = 0; i < 180; i++)
		{
			positions[i] = new JLabel("" + i);
			GameWindow.add(positions[i]);
		}
		//Adds the player
		positions[player.position].setText("O");
		
		//Adds the static ground
		JLabel ground = new JLabel("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		Ground.add(ground);
		this.add(Ground);
	}
	
	public void GenerateBlocks()
	{
		if (timeUntilNextGenerate > 0)  //If we still need to wait, decrease the time and continue
		{
			timeUntilNextGenerate -= 1;
			return;
		}
		
		if (randomNumber.nextInt(5) == 0) //20% chance
		{
			int blocksToUse = randomNumber.nextInt(3) + 1; //Random number between 1 and 3
			
			List<Integer> emptyBlocks = new ArrayList<Integer>(); //Storing the positions of empty blocks
			
			int currentBlockChecker = 0; //Variable for current block checking, used for finding empty blocks
			while (emptyBlocks.size() != blocksToUse)
			{
				if (blocks[currentBlockChecker] == null)
				{
					System.out.println(currentBlockChecker + " is null");
					emptyBlocks.add(currentBlockChecker);
				}
				else if (!blocks[currentBlockChecker].isActive) //If the block is not in use
				{
					System.out.println(currentBlockChecker + " is active: " + blocks[currentBlockChecker].isActive);
					emptyBlocks.add(currentBlockChecker);  //Holds all the positions of the not used blocks
				}
				currentBlockChecker += 1;
			}
			
			int height = 0; //For stacking blocks on top of each other
			for (int blockPosition : emptyBlocks) //For each empty block position...
			{
				System.out.println("Empty block position is: " + blockPosition);
				blocks[blockPosition] = new Block(179 - (height * 20));
				blocks[blockPosition].isActive = true;
				height += 1;
			}
			
			timeUntilNextGenerate = 8; //Forces game to wait at least 8 updates before another generation can happen
			
		}
	}
	

	public void UpdateGame()
	{
		for (int i = 0; i < 180; i++)  //Resets the board
		{
			positions[i].setText("");
		}
		
		//Updates player position
		if (player.firstJump)
		{
			player.firstJump = false;
		}
		else if (player.isComingDown)
		{
			player.position += 20;
			if (player.position <= 92           )
			{
				player.isComingDown = true;
			}
			if (player.position == 162)
			{
				player.isComingDown = false;
			}
		}
		else if (!player.isComingDown && player.position != 162)
		{
			player.position -= 20;
			if (player.position <= 92)
			{
				player.isComingDown = true;
			}
			if (player.position == 162)
			{
				player.isComingDown = false;
			}
		}
		positions[player.position].setText("O");
		
		//Updates blocks and position
		for (int i = 0; i < 200; i++)
		{
			if (blocks[i] == null)  //If the block is not initiated or null, continue
			{
				continue;
			}
			else if (!blocks[i].isActive)  //If the block is off screen, continue
			{
				continue;
			}
			else  //Block is active
			{
				blocks[i].moveNextPos();
				if (!blocks[i].isActive)  //Checks if the block should be off-screen
				{
					blocks[i].isActive = false;
					continue;
				}
				else
				{
					positions[blocks[i].position].setText(Block.TEXT);
					
					//Checks for collision between player and a block
					if (blocks[i].position == player.position)
					{
						System.out.println("You lost!");
						System.exit(1);
					}
				}								
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == 38 || e.getKeyCode() == 32)  // 38 is up arrow, and 32 is space bar
		{
			System.out.println("Jumped!");
			
			player.Jump();
			
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{		
	}
	
	@Override
	public void keyTyped(KeyEvent e) 
	{		
	}

}
