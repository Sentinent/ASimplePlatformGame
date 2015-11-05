import java.awt.BorderLayout;
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
		
		//Adds a block at position 98
		blocks[0] = new Block(98);
		positions[98].setText(Block.TEXT);
	}
	

	public void UpdateGame()
	{
		for (int i = 0; i < 180; i++)  //Resets the board
		{
			positions[i].setText("" + i);
		}
		
		//Updates player position
		if (player.firstJump)
		{
			player.firstJump = false;
		}
		else if (player.isComingDown)
		{
			player.position += 20;
			if (player.position <= 102)
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
			if (player.position <= 102)
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
			else if (!blocks[i].isActive)  //If the block is off screen, set it to null
			{
				blocks[i] = null;
			}
			else  //Block is active
			{
				blocks[i].moveNextPos();
				if (!blocks[i].isActive)  //Checks if the block sould be offscreen
				{
					blocks[i] = null;
				}
				else
				{
					positions[blocks[i].position].setText(Block.TEXT);
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
