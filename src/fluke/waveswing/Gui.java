package fluke.waveswing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import fluke.waveswing.wfc.PaintTileEditor;

public class Gui implements MouseListener
{
	public static int canvasWidth = 1200;
	public static int canvasHeight = 700;
	public static int tileEditorWidth = 200;
	public static int tileEditorHeight = 300;
	public static boolean reset = false;
	public static boolean runSlow = false;
	public static boolean giveTheFuckUp = false;
	public static String tileset = "tileset.png";
	private JFileChooser fc = new JFileChooser();
	private JFrame tileEditor = new JFrame();
	public static int tileEditorIndex = 0;
	
	public Gui() throws InterruptedException
	{
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setupTileEditor();
		
		JPanel painterPane = new JPanel();
		painterPane.setLayout(new BoxLayout(painterPane, BoxLayout.Y_AXIS));
		PaintCanvas painter = new PaintCanvas();
		painterPane.add(painter);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		JButton bReset = new JButton("Restart");
		JButton bStop = new JButton("Stop");
		JButton bSave = new JButton("Save");
		JCheckBox slowCheck = new JCheckBox("Slow Down");
		JComboBox<String> comboTileset = new JComboBox<String>(new String[] {"tileset.png", "tileset_wall_dirt.png", "tileset_dungeon.png", "tileset_full.png", "tileset_full_no_streams.png", "Load from file..."});
		comboTileset.setMaximumSize(comboTileset.getPreferredSize());
		buttonPane.add(bReset);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(bStop);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(bSave);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(slowCheck);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(comboTileset);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		
		JPanel grouphug = new JPanel();
		grouphug.setLayout(new BoxLayout(grouphug, BoxLayout.Y_AXIS));
		grouphug.add(painterPane);
		grouphug.add(buttonPane);
		
		f.add(grouphug);
		f.addMouseListener(this);
		f.setSize(canvasWidth, canvasHeight);
		f.setVisible(true);
		
		bReset.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//will restart new run when current one is finished
				reset = true;		
			}          
		});
		
		bStop.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//end current run
				giveTheFuckUp = true;			
			}          
		});
		
		bSave.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				saveMapToFile();			
			}          
		});
		
		comboTileset.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				tileset = (String)comboTileset.getSelectedItem();
				if(tileset.equals("Load from file..."))
				{
					int returnVal = fc.showOpenDialog(f);
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
		                File file = fc.getSelectedFile();
		                tileset = file.getAbsolutePath();
		            }
					else
					{
						comboTileset.setSelectedIndex(0);
						tileset = (String)comboTileset.getSelectedItem();
					}
				}
			}          
		});
		
		slowCheck.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				//adds delay in between placing tiles
				runSlow = e.getStateChange() == 1 ? true : false;
			}
		});

		for(;;)
		{
			if(reset)
			{
				Main.reset(tileset);
				reset = false;
				giveTheFuckUp = false;
			}
			
			while(!Main.wfc.allTilesSet() && !giveTheFuckUp) 
			{
				if(runSlow)
					Thread.sleep(15);
				
				if(Main.wfc.collapseNext())
				{
					painter.repaint();
				}
				else //no tile was possible at location, try again
				{
					reset = true;
					giveTheFuckUp = true;
				}
			}
			Thread.sleep(100);
		}
	}
	
	public void setupTileEditor()
	{
		JPanel tilePainterPane = new JPanel();
		tilePainterPane.setLayout(new BoxLayout(tilePainterPane, BoxLayout.Y_AXIS));
		PaintTileEditor tePainter = new PaintTileEditor();
		tilePainterPane.add(tePainter);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		JLabel lWeight = new JLabel("Edit Weight");
		JTextField tfWeight = new JTextField("0", 8);
		tfWeight.setMaximumSize(tfWeight.getPreferredSize());
		JButton bSave = new JButton("Save");
		JButton bDelete = new JButton("Delete Tile");
		buttonPane.add(lWeight);
		buttonPane.add(tfWeight);
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));
		buttonPane.add(bSave);
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));
		buttonPane.add(bDelete);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		
		JPanel grouphug = new JPanel();
		grouphug.setLayout(new BoxLayout(grouphug, BoxLayout.Y_AXIS));
		grouphug.add(tilePainterPane);
		grouphug.add(buttonPane);
		
		tileEditor.add(grouphug);
		tileEditor.setSize(tileEditorWidth, tileEditorHeight);
		tileEditor.setLocation(canvasWidth, 0);
//		tileEditor.setVisible(true);
		
		bSave.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				String text = tfWeight.getText();
				try {
					Main.tileset.tiles[tileEditorIndex].weight = Integer.parseInt(text);
					reset = true;
					tileEditor.setVisible(false);
				} catch(NumberFormatException e){  
				}
			}          
		});
		
		bDelete.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				Main.tileset.removeTileFromSet(tileEditorIndex);	
				reset = true;
				tileEditor.setVisible(false);
			}          
		});
	}
	
	public void saveMapToFile()
	{
		BufferedImage map = PaintCanvas.paintImage.getSubimage(PaintCanvas.border, PaintCanvas.border, PaintCanvas.tileSize*Main.mapTileSize, PaintCanvas.tileSize*Main.mapTileSize);
		try
		{
			ImageIO.write(map, "PNG", new File("krunkermap.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		JsonMap.createJsonMap(map);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int index = PaintCanvas.getTileIndexClicked(e.getX(), e.getY());	
		if(index != -1)
		{
			tileEditorIndex = index;
			tileEditor.setVisible(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{	
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{		
	}

}
