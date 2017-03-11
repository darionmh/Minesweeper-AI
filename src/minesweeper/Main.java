package minesweeper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static java.awt.event.KeyEvent.*;


public class Main {
	public static void main(String vars[]) throws AWTException, IOException{
		Runtime runtime = Runtime.getRuntime();

        runtime.exec( "Minesweeper.exe" );
        
        Robot robot = new Robot();
		robot.delay(2000);
        
        final Dimension screenSize = Toolkit.getDefaultToolkit().
            getScreenSize();
        final BufferedImage screen = robot.createScreenCapture(
            new Rectangle(screenSize));
        
        ArrayList<Double> points = new ArrayList<Double>();
        
      //16, 102
        //141, 227
        
        points.add(12.0);
        points.add(95.0);
        points.add(141.0);
        points.add(227.0);
        
        solve(points.get(0), points.get(1), points.get(2), points.get(3));

        /*SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JLabel screenLabel = new JLabel(new ImageIcon(screen));
                JScrollPane screenScroll = new JScrollPane(screenLabel);
                screenScroll.setPreferredSize(new Dimension(
                    (int)(screenSize.getWidth()/2),
                    (int)(screenSize.getHeight()/2)));

                final Point pointOfInterest = new Point();

                JPanel panel = new JPanel(new BorderLayout());
                panel.add(screenScroll, BorderLayout.CENTER);

                final JLabel pointLabel = new JLabel(
                    "Click on any point in the screen shot!");
                panel.add(pointLabel, BorderLayout.SOUTH);

                screenLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        pointOfInterest.setLocation(me.getPoint());
                        pointLabel.setText(
                            "Point: " +
                            pointOfInterest.getX() +
                            "x" +
                            pointOfInterest.getY());
                        points.add(pointOfInterest.getX());
                        points.add(pointOfInterest.getY());
                    }
                });

                JOptionPane.showMessageDialog(null, panel);

                System.out.println("Point of interest: " + pointOfInterest);
                
                try {
					solve(points.get(0), points.get(1), points.get(2), points.get(3));
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });*/
        
        
        
        
	
	}
	
	public static int w;
	public static int h;
	public static boolean nei = false;
	public static void solve(double l, double t, double r, double b) throws AWTException{
		Scanner in = new Scanner(System.in);
		Random rand = new Random();
		HashMap<Integer, Integer> hexToVal = new HashMap<Integer, Integer>();
		hexToVal.put(-1, -10);	
		hexToVal.put(-65793, -9);	
		hexToVal.put(-8355712, 0);
		hexToVal.put(-4144960, 0);
		hexToVal.put(-16776961, 1);
		hexToVal.put(-16744448, 2);
		hexToVal.put(-65536, 3);
		hexToVal.put(-16777088, 4);
		hexToVal.put(-16777216, -11);
		int rows = 8;
		int cols = 8;
		
		Point[][] points = new Point[rows][cols];
		int[][] board = new int[rows][cols];
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				board[i][j] = -10;
			}
		}
		
		Robot robot = new Robot();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
		
        int top = 0;
        int left = 0;
		for(int x=(int)l;x<r;x++){
			for(int y=(int)t;y<b;y++){
				//robot.mouseMove(x, y);
				//robot.delay(5);
				int color = screen.getRGB(x, y);
				if(screen.getRGB(x, y)==-1){
					left = x;
					top = y;
					break;
				}
			}
			if(top != 0)
				break;
		}
		
		ArrayList<Integer> ys = new ArrayList<Integer>();
		ys.add(top);
		ArrayList<Integer> xs = new ArrayList<Integer>();
		xs.add(left);
		
		//robot.mouseMove(xs.get(0), ys.get(0));
		//robot.delay(500);
		
		boolean ready = false;
		for(int x=left;x<screenSize.getWidth();x++){
			//System.out.println(top+" "+x);
			if(screen.getRGB(x, top) != -1)
				ready = true;
			if(ready && screen.getRGB(x, top) == -1){
				xs.add(x);
				ready = false;
			}
			
			if(xs.size()==8)
				break;
		}
		
		ready = false;
		for(int y=top;y<screenSize.getHeight();y++){
			if(screen.getRGB(left, y) != -1)
				ready = true;
			if(ready && screen.getRGB(left, y) == -1){
				ys.add(y);
				ready = false;
			}
			if(ys.size()==8)
				break;
		}
		
		int rw = 0;
		int c = 0;
		for(int x:xs){
			for(int y:ys){
				points[rw][c] = new Point(x,y);
				rw++;
			}
			rw=0;
			c++;
		}
		
		System.out.println(xs.size());
		System.out.println(ys.size());
		
		rw = 0;
		c = 0;
		for(Point[] row:points){
			System.out.println(rw+" "+c);
			for(Point p:row){
				//robot.mouseMove((int)p.getX(), (int)p.getY());
				//robot.delay(150);
			}
			rw++;
			c=0;
		}
		
		w = (int) (points[0][1].getX() - points[0][0].getX());
		h = (int) (points[1][0].getY() - points[0][0].getY());
		
		System.out.println(w+"x"+h);
		
		System.out.println("\n");

		
		robot.mouseMove((int)points[3][3].getX(), (int)points[3][3].getY());
		robot.mousePress(BUTTON1_DOWN_MASK);
		robot.mouseRelease(BUTTON1_DOWN_MASK);
		
		robot.delay(100);
		
		updateBoard(board, points, hexToVal);
		
		for(int[] row:board){
			for(int i:row){
				//System.out.print(i+" ");
			}
			//System.out.println();
		}
		
		ArrayList<String> bombList = new ArrayList<String>();
		while(bombList.size() < 10){
			nei = true;
			//System.out.println(bombList.size());
			//Go through board, finding all numbers with identical amount of surrounding space
			Stack<int[]> stack = new Stack<int[]>();
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(board[i][j] > 0)
						stack.push(new int[] {i,j});
				}
			}
			
			while(!stack.isEmpty()){
				int[] curr = stack.pop();
				
				ArrayList<int[]> bombs = new ArrayList<int[]>();
				
				for(int x=curr[0]-1;x<=curr[0]+1;x++){
					if(x < 0 || x >= rows)
						continue;
					for(int y=curr[1]-1;y<=curr[1]+1;y++){
						if(y < 0 || y >= cols || (x==curr[0] && y==curr[1]))
							continue;
						
						if(board[x][y] == -10 || board[x][y] == -1)
							bombs.add(new int[] {x,y});
					}
				}
				
				if(bombs.size() == board[curr[0]][curr[1]]){
					for(int[] bomb:bombs){
						int x = bomb[0];
						int y = bomb[1];
						if(!bombList.contains(x+" "+y)){
							bombList.add(x+" "+y);
							//robot.mouseMove((int)points[x][y].getX()+w/2, (int)points[x][y].getY()+h/2);
							//robot.mousePress(BUTTON3_DOWN_MASK);
							//robot.mouseRelease(BUTTON3_DOWN_MASK);
						}
						board[x][y] = -1;
						
					}
				}
			}
			
			//Go around all numbers, clicking safe spots
			stack.clear();
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(board[i][j] > 0)
						stack.push(new int[] {i,j});
				}
			}
			
			while(!stack.isEmpty()){
				int[] curr = stack.pop();
				
				ArrayList<int[]> bombs = new ArrayList<int[]>();
				
				for(int x=curr[0]-1;x<=curr[0]+1;x++){
					if(x < 0 || x >= rows)
						continue;
					for(int y=curr[1]-1;y<=curr[1]+1;y++){
						if(y < 0 || y >= cols || (x==curr[0] && y==curr[1]))
							continue;
						
						if(board[x][y] == -1)
							bombs.add(new int[] {x,y});
					}
				}
				
				if(bombs.size() == board[curr[0]][curr[1]]){
					//System.out.printf("SAFE - EQUAL: %d %d\n", curr[0], curr[1]);
					for(int x=curr[0]-1;x<=curr[0]+1;x++){
						if(x < 0 || x >= rows)
							continue;
						for(int y=curr[1]-1;y<=curr[1]+1;y++){
							if(y < 0 || y >= cols || (x==curr[0] && y==curr[1]))
								continue;
							
							if(board[x][y] <= -2){
								//System.out.printf("Click: %d %d value=%d\n", x, y, board[x][y]);
								click(points[x][y].getX(), points[x][y].getY());
							}
						}
					}
				}
			}
			
			if(nei){				
				int x,y;
				do{
					x = rand.nextInt(rows);
					y = rand.nextInt(cols);
				}while(board[x][y] != -10);
				
				System.out.println("NEI");
				click(points[x][y].getX(), points[x][y].getY());
			}
			
			robot.delay(100);
			
			updateBoard(board, points, hexToVal);
			
		}
		
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(board[i][j] == -10)
					click(points[i][j].getX(), points[i][j].getY());
			}
		}
		
		System.out.println("Success! :)");
		in.close();
	}
	
	static boolean gameover = false;
	static long lastTime = 0;
	public static int[][] updateBoard(int[][] board, Point[][] points, HashMap<Integer, Integer> hexToVal){
		Scanner in = new Scanner(System.in);
		int rows = 8;
		int cols = 8;
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
        
        
        boolean print = false;
		if(System.currentTimeMillis()-lastTime > 1000){
			print = true;
			lastTime = System.currentTimeMillis();
			System.out.println();
		}
		
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				int color = findColor(screen, points[i][j], w, h);
				
				if(!hexToVal.containsKey(color)){
					System.out.printf("What does %d mean (located at %d, %d)? ", color, i, j);
					int val = in.nextInt();
					
					hexToVal.put(color, val);
				}
				
				if(color != -1)
					board[i][j] = hexToVal.get(color);
				
				if(print)
					System.out.printf("%4d",board[i][j]);
				
				if(board[i][j] == -11)
					gameover = true;
			}
			if(print)
				System.out.println();
		}
		
		if(gameover){
			System.out.println("Game over");
			System.exit(0);
		}
		
		return board;
	}
	
	public static void click(double x, double y){
		click((int)x, (int)y);
	}
	
	public static void click(int x, int y){
		nei = false;
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(x+w/3, y+h/3);
			robot.mousePress(BUTTON1_DOWN_MASK);
			robot.delay(100);
			robot.mouseRelease(BUTTON1_DOWN_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void _solve(double l, double t, double r, double b) throws AWTException{
		Scanner in = new Scanner(System.in);
		Random rand = new Random();
		HashMap<Integer, Integer> hexToVal = new HashMap<Integer, Integer>();
		hexToVal.put(-1, -9);	
		hexToVal.put(-65793, -9);	
		hexToVal.put(-8355712, 0);
		hexToVal.put(-4144960, 0);
		hexToVal.put(-16776961, 1);
		hexToVal.put(-16744448, 2);
		hexToVal.put(-65536, 3);
		hexToVal.put(-16777088, 4);
		hexToVal.put(-16777216, -11);
		int rows = 8;
		int cols = 8;
		
		int w = (int) ((r-l)/cols);
		int h = (int) ((b-t)/rows);
		
		
		
		Point[][] points = new Point[rows][cols];
		int[][] board = new int[rows][cols];
		
		Robot robot = new Robot();
		final Dimension screenSize = Toolkit.getDefaultToolkit().
	            getScreenSize();
        BufferedImage screen = robot.createScreenCapture(
            new Rectangle(screenSize));
		
		for(int row=0;row<rows;row++){
			for(int col=0;col<cols;col++){
				int cL = (int)Math.floor(l+(col*w));
				int cT = (int)Math.floor(t+(row*h));				
				
				points[row][col] = new Point(cL, cT);
				board[row][col] = -10;
				
				//System.out.printf("%4d", findColor(screen, points[row][col], w, h));
			}
//			System.out.println();
		}
		
	//	System.out.println("\n");

		
		robot.mouseMove((int)points[0][0].getX(), (int)points[0][0].getY());
		robot.mousePress(BUTTON1_DOWN_MASK);
		robot.mouseRelease(BUTTON1_DOWN_MASK);
		
		robot.delay(100);
		screen = robot.createScreenCapture(new Rectangle(screenSize));
		
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				int color = findColor(screen, points[i][j], w, h);
				
				if(!hexToVal.containsKey(color)){
					System.out.printf("What does %d mean (located at %d, %d)? ", color, i, j);
					int val = in.nextInt();
					
					hexToVal.put(color, val);
				}
				
				if(color != -1){
					board[i][j] = hexToVal.get(color);
				}
				
				System.out.printf("%4d", board[i][j]);
			}
			
			System.out.println();
		}
		
		for(int[] row:board){
			for(int i:row){
				//System.out.print(i+" ");
			}
			//System.out.println();
		}
		
		ArrayList<String> bombList = new ArrayList<String>();
		boolean gameover = false;
		long lastTime = 0;
		while(bombList.size() < 10){
			//System.out.println(bombList.size());
			//Go through board, finding all numbers with identical amount of surrounding space
			Stack<int[]> stack = new Stack<int[]>();
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(board[i][j] > 0)
						stack.push(new int[] {i,j});
				}
			}
			
			while(!stack.isEmpty()){
				int[] curr = stack.pop();
				
				ArrayList<int[]> bombs = new ArrayList<int[]>();
				
				for(int x=curr[0]-1;x<=curr[0]+1;x++){
					if(x < 0 || x >= rows)continue;
					for(int y=curr[1]-1;y<=curr[1]+1;y++){
						if(y < 0 || y >= cols || (x==curr[0] && y==curr[1]))continue;
						
						if(board[x][y] == -10 || board[x][y] == -1)
							bombs.add(new int[] {x,y});
					}
				}
				
				if(bombs.size() == board[curr[0]][curr[1]]){
					for(int[] bomb:bombs){
						int x = bomb[0];
						int y = bomb[1];
						if(!bombList.contains(x+" "+y)){
							bombList.add(x+" "+y);
							/*robot.mouseMove((int)points[x][y].getX()+w/2, (int)points[x][y].getY()+h/2);
							robot.mousePress(BUTTON3_DOWN_MASK);
							robot.mouseRelease(BUTTON3_DOWN_MASK);*/
						}
						board[x][y] = -1;
						
					}
				}
			}
			
			//Go through every space, increasing score 
			stack.clear();
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(board[i][j] == -10)
						stack.push(new int[] {i,j});
				}
			}
			
			while(!stack.empty()){
				int[] curr = stack.pop();
				
				for(int x=curr[0]-1;x<=curr[0]+1;x++){
					if(x < 0 || x >= rows)continue;
					for(int y=curr[1]-1;y<=curr[1]+1;y++){
						if(y < 0 || y >= cols || (x==curr[0] && y==curr[1]))continue;
						
						if(board[x][y] > 0)
							board[curr[0]][curr[1]]++;
					}
				}
			}
			
			
			//Get a list of numbers here
			stack.clear();
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(board[i][j] > 0)
						stack.push(new int[] {i,j});
				}
			}
			
			boolean nei = false;
			//Go through numbers, picking out bombs
			while(!stack.empty()){
				int[] curr = stack.pop();
				int count = board[curr[0]][curr[1]];
				
				//Go around all adjacent cells, getting the x biggest numbers 
				//where x is equal to the number of adjacent mines
				ArrayList<int[]> biggest = new ArrayList<int[]>();
				ArrayList<int[]> notBombs = new ArrayList<int[]>();
				ArrayList<Integer> values = new ArrayList<Integer>();
				for(int x=curr[0]-1;x<=curr[0]+1;x++){
					if(x < 0 || x >= rows)continue;
					for(int y=curr[1]-1;y<=curr[1]+1;y++){
						if(y < 0 || y >= cols || (y==curr[1] && x==curr[0]))continue;
						
						if(board[x][y] >= 0)continue;
						
						values.add(board[x][y]);
						
						if(biggest.size() < count){
							biggest.add(new int[] {x,y});
						}else{
							int[] min = biggest.get(0);
							int minI = 0;
							for(int i=1;i<count;i++){
								int[] big = biggest.get(i);
								if(board[min[0]][min[1]] < board[big[0]][big[1]]){
									min = big;
									minI = i;
								}
							}
							if(board[x][y] > board[min[0]][min[1]]){
								notBombs.add(biggest.get(minI));
								biggest.remove(minI);
								biggest.add(minI, new int[] {x,y});
							}else{
								notBombs.add(new int[] {x,y});
							}
						}
					}
				}
				
				if(values.size()>0){
					int min = values.get(0);
					int minCount = 1;
					for(int i=1;i<values.size();i++){
						if(values.get(i) < min){
							min=values.get(i);
							minCount = 1;
						}else if(values.get(i) == min)minCount++;
					}
					nei = minCount > count;
				}
				
				
				if(nei)continue;
				
				for(int[] bomb:biggest){
					int x = bomb[0];
					int y = bomb[1];
					
					if(!bombList.contains(x+" "+y)){
						bombList.add(x+" "+y);
						/*robot.mouseMove((int)points[x][y].getX()+w/2, (int)points[x][y].getY()+h/2);
						robot.mousePress(BUTTON3_DOWN_MASK);
						robot.mouseRelease(BUTTON3_DOWN_MASK);*/
					}
					board[x][y] = -1;
				}
				
				for(int[] safe:notBombs){
					int x = safe[0];
					int y = safe[1];
					
					robot.mouseMove((int)points[x][y].getX()+w/2, (int)points[x][y].getY()+h/2);
					robot.mousePress(BUTTON1_DOWN_MASK);
					robot.mouseRelease(BUTTON1_DOWN_MASK);
				}
			}
			
			if(nei){
				System.out.println();
				for(int[] row:board){
					for(int i:row){
						System.out.printf("%4d", i);
					}
					System.out.println();
				}
				
				int x,y;
				do{
					x = rand.nextInt(rows);
					y = rand.nextInt(cols);
				}while(board[x][y] != -10);
				
				System.out.println("NEI");
				robot.mouseMove((int)points[x][y].getX()+w/2, (int)points[x][y].getY()+h/2);
				robot.mousePress(BUTTON1_DOWN_MASK);
				robot.mouseRelease(BUTTON1_DOWN_MASK);
			}
			
			robot.delay(100);
			screen = robot.createScreenCapture(new Rectangle(screenSize));
			
			boolean print = false;
			if(System.currentTimeMillis()-lastTime > 5000){
				print = true;
				lastTime = System.currentTimeMillis();
				System.out.println();
			}
			
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					int color = findColor(screen, points[i][j], w, h);
					
					if(!hexToVal.containsKey(color)){
						System.out.printf("What does %d mean (located at %d, %d)? ", color, i, j);
						int val = in.nextInt();
						
						hexToVal.put(color, val);
					}
					
					if(color != -1){
						board[i][j] = hexToVal.get(color);
					}
					
					if(print)
						System.out.printf("%4d",board[i][j]);
					
					if(board[i][j] == -11)gameover = true;
				}
				if(print)
					System.out.println();
			}
			
			if(gameover){
				System.out.println("Game over");
				System.exit(0);
			}
			
		}
		
		in.close();
	}
	
	public static int findColor(BufferedImage image, Point tl, int w, int h){
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(-16776961);
		colors.add(-16744448);
		colors.add(-65536);
		colors.add(-16777088);
		colors.add(-1);	
		colors.add(-65793);
		colors.add(-8355712);
		colors.add(-4144960);
		colors.add(-16777216);
		
		int minColor = image.getRGB((int)tl.getX(), (int)tl.getY());
		double blackCount = 0;
		int whiteCount = 0;
		int one = 0, two = 0, three = 0, four = 0, five = 0, six = 0, seven = 0, eight = 0;
		
		//System.out.printf("Finding color for %f %f\n", tl.getX(), tl.getY());
		for(int x=(int) tl.getX();x<tl.getX()+w;x++){
			for(int y=(int)tl.getY();y<tl.getY()+h;y++){
				int color = image.getRGB(x, y);
				//System.out.printf("%d ", color);
				if(!colors.contains(color)){
					System.exit(0);
				}
				if(color==-16777216)blackCount++;
				if(color==-16776961)one++;
				if(color==-16744448)two++;
				if(color==-65536)three++;
				if(color==-16777088)four++;
				if(color<minColor)minColor=color;
				if(color==-1 || color==-65793){whiteCount++;
					//System.out.println("WHITE: "+whiteCount);
				}
			}
		}

		if(blackCount/(w*h)>0.0)return -16777216;
		
		double[] perc = {one,two,three,four};
		
		double max = perc[0];
		int maxI = 0;
		for(int i=1;i<perc.length;i++){
			if(perc[i]>max){
				max = perc[i];
				maxI = i;
			}
		}
		if(max/(w*h)>0.0)return colors.get(maxI);
		
		//System.out.println("should return white if it exists");
		if(whiteCount > 0)return -1;
		
		//if(whiteCount > 0)System.out.println("ERRRRRRRRRRRRRRRRR");
		//System.out.println("Didn't do white");
		//System.out.println();
		//System.out.println("Returning min - whiteCOunt: "+whiteCount);
		return minColor;
	}
	/*
	 * Take a snapshot of the screen using the Java Robot class.
	 * Return the screen shot as an Image (BufferedImage).
	 *
	 */
	public static BufferedImage getBackgroundImage()
	{
	  try {
	    Robot rbt = new Robot();
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension dim = tk.getScreenSize();
	    BufferedImage background = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
	    return background;
	    //return new ImageIcon(background);
	  }
	  catch (Exception ex) {
	    ex.printStackTrace();
	  }
	  return null;
	}
}