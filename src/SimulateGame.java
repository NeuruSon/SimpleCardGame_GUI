/*
 * Matt Levan
 * CSC 331, Dr. Amlan Chatterjee
 * Data Structures
 *
 * Project 3 -- Simple Card Game
 *
 * SimulateGame.java
 * Main class for running the simple card game.
 *
 * A simple card game with an option for two players.
 * The deck of cards contains 52 cards with 13 cards each in the 4 suits:
 * clubs, diamonds, hearts, spades.
 * 
 * Each player begins with 26 cards and one of the players starts the game
 * by putting their first card on the table. Players take turns by putting the
 * top card from their hand, until the current card placed on the table matches
 * the suit of the previous card on the table. If a match happens, the player
 * whose card matched gets all the cards currently on the table and adds them
 * at the end of the cards currently in his or her hand. Game continues until
 * one player gets all 52 cards, or for 10 rounds.
 *
 * Construct the game using the following guidelines:
 *
 * 1. Create a method to deal the deck of cards so that each player gets 26
 * random cards.
 *
 * 2. Start the game by choosing either of the player randomly. 
 *
 * 3. Show the cards on the table and in the hand of each player at each step
 * of the game.
 *
 * 4. Continue the game for 10 rounds or until one player has all the cards, 
 * whichever happens first.
 *
 * 5. Declare the winner (the player with all the cards, or with more cards 
 * after 10 rounds), or say its a tie (when both players have equal number
 * of cards after 10 rounds).
 *
 * Must use at least one singly linked list, one 2D array, methods to separate
 * work (main method should not have more than 20 lines of code).
 *
 * Due in full by 11/16/2015 @ 11:59 PM.
 *
 */

// Import Random
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class SimulateGame{ // extends JFrame
    // Attributes

    private static Player playerOne = new Player("플레이어");
    private static Player playerTwo = new Player("앨리스");
    private static Player currentPlayer = playerOne;
    private static Deck deck = new Deck(true); // Shuffle deck automatically
    private static ArrayList<Card> table = new ArrayList<>();
    private static Card topCard;
   
//    private static int roundsPlayed = 1;
    private static boolean gameOver = true;
    
    //임의로 추가한 variable 
    private static int roundsPlayed = 0; //플레이 '한' 라운드가 아니라 플레이'중'인 라운드 표시 
    private static int xPosR = 200;
    private static int yPosR = 100;
    private static int xPosC = 640;
    private static int yPosC = 430;
    private static int xPosP = 1100;
    private static int yPosP = 740;
    private static int wCard = 200;
    private static int hCard = 300;
    
    private static int gameLogNum, madAlice = 0; 
    private static int delay = 1000;
    private static boolean isPlayerzTurn, isHolding, canPressBell, isBellPressed, pressFlag = false; 
    private static JFrame noreumFrame;
    private static JButton startBtn, bellBtn;
    private static JPanel noreumPanel, gameLogPanel;
    private static JLayeredPane cardPane;
    private static JScrollPane gameLogScroll;
    private static JTextArea gameLog;
    private static JLabel leftCardP, leftCardR, topCardP, topCardC, currentRound, currentRoundLabel;
    private static JLabel howToPlay, howToPlay1, howToPlay2, howToPlay3;
    private JLabel cardImg;
    EventListeners listener = new EventListeners();

    private static Card cardToPlay;
    static LoadCardImg tempC1, tempP1, tempP2, tempR1, tempR2;
    
    // Main method

    public static void main(String[] args) {
    	SimulateGame s = new SimulateGame(); //게임은 SimulateGame() 안에서 돌아감. 
    }

    
    // Methods
	
    public SimulateGame() {
    	noreumFrame = new JFrame();
    	noreumFrame.setSize(2000, 1125 + 22);
    	noreumFrame.setLayout(null);
    	noreumFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	noreumFrame.setResizable(false);
    	noreumFrame.setTitle("트럼프 할리갈리");
    	
    	noreumPanel = new JPanel();
    	Color noreumColor = new Color(39, 105, 72);
    	noreumPanel.setBackground(noreumColor);
    	noreumPanel.setLayout(null);
    	noreumPanel.setBounds(0, 0, 2000, 1125);
    	
    	cardPane = noreumFrame.getLayeredPane();
    	cardPane.setPreferredSize(new Dimension(2000, 1125));
    	cardPane.addMouseListener(listener); //press 감지 
    	cardPane.addMouseMotionListener(listener); //drag 감지 
    	
		Font logFont = new Font("DungGeunMo", Font.BOLD, 20);    	
    	Font startBtnFont = new Font("DungGeunMo", Font.BOLD | Font.ITALIC, 40);
    	Font numFont = new Font("DungGeunMo", Font.BOLD | Font.ITALIC, 80);
    	Font roundFont = new Font("DungGeunMo", Font.BOLD | Font.ITALIC, 250);
    	
    	startBtn = new JButton("START NEW GAME ");
    	startBtn.setFont(startBtnFont);
    	startBtn.addActionListener(listener);
    	startBtn.setBounds(1300, 450, 500, 100);
    	
    	int wGameLog = 700;
    	int hGameLog = 300;
    	gameLogPanel = new JPanel();
    	gameLogPanel.setBackground(Color.BLACK);
    	gameLogPanel.setLayout(null);
    	gameLogPanel.setBounds(1050, 100, wGameLog + 13, hGameLog + 10);
		
    	TitledBorder gameLogBorder = new TitledBorder(new LineBorder(Color.white, 2), "GAME LOG");
    	gameLogBorder.setTitleColor(Color.white);
    	gameLogBorder.setTitleFont(logFont);
    	
    	MatteBorder gameLogAreaBorder = new MatteBorder(10, 20, 15, 20, Color.BLACK);
    	
    	gameLog = new JTextArea();
		gameLog.setFont(logFont);
		gameLog.setForeground(Color.white);
		gameLog.setBackground(Color.black);
		gameLog.setBorder(gameLogAreaBorder);
    	gameLog.setEditable(false);
    	
    	gameLogScroll = new JScrollPane(gameLog, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	gameLogScroll.setBounds(7, 2, wGameLog, hGameLog); 
    	gameLogScroll.setBackground(Color.BLACK);
    	gameLogScroll.setViewportView(gameLog);
    	gameLogScroll.setAutoscrolls(true);
    	gameLogScroll.setBorder(gameLogBorder);
    	
    	leftCardP = new JLabel();
    	leftCardP.setFont(numFont);
    	leftCardP.setForeground(Color.WHITE);
    	leftCardP.setBounds(xPosC + 50, yPosC + hCard, 200, 100);
    	
    	leftCardR = new JLabel();
    	leftCardR.setFont(numFont);
    	leftCardR.setForeground(Color.WHITE);
    	leftCardR.setBounds(xPosC + 50, yPosC - 100, 200, 100);

    	currentRound = new JLabel("0");
    	currentRound.setFont(roundFont);
    	currentRound.setForeground(Color.WHITE);
    	currentRound.setBounds(100, 700, 1000, 500);
    	
    	currentRoundLabel = new JLabel("ROUND");
    	currentRoundLabel.setFont(numFont);
    	currentRoundLabel.setForeground(Color.WHITE);
    	currentRoundLabel.setBounds(100, 600, 1000, 400);
    	
    	howToPlay = new JLabel("HOW TO PLAY");
    	howToPlay.setFont(startBtnFont);
    	howToPlay.setForeground(Color.WHITE);
    	howToPlay.setBounds(1750, 833, 1000, 400);
    	
    	howToPlay1 = new JLabel("\"마이 턴!\" 차례가 되면 하단의 카드를 가운데 카드 더미로 드래그 해주세요.");
    	howToPlay1.setFont(logFont);
    	howToPlay1.setHorizontalTextPosition(SwingConstants.RIGHT); 
    	howToPlay1.setForeground(Color.WHITE);
    	howToPlay1.setBounds(1260, 868, 1000, 400);
    	
    	howToPlay2 = new JLabel("더미 위에 올라가 있던 카드와 방금 올려둔 카드의 문양이 같으면 빠르게 종 버튼을 눌러주세요.");
    	howToPlay2.setFont(logFont);
    	howToPlay2.setHorizontalTextPosition(SwingConstants.RIGHT); 
    	howToPlay2.setForeground(Color.WHITE);
    	howToPlay2.setBounds(1080, 898, 1000, 400);
    	
    	howToPlay3 = new JLabel(" 로그를 보며 천천히 플레이 해주세요.");
    	howToPlay3.setFont(startBtnFont);
    	howToPlay3.setForeground(Color.WHITE);
    	howToPlay3.setBounds(1050, 35, 1000, 60);
    	
    	LoadCardImg posCardP = new LoadCardImg("noreum_back");
    	posCardP.setImgBounds(xPosP, yPosP);
    	LoadCardImg posCardC = new LoadCardImg("noreum_back");
    	posCardC.setImgBounds(xPosC, yPosC);
    	LoadCardImg posCardR = new LoadCardImg("noreum_back");
    	posCardR.setImgBounds(xPosR, yPosR);
    	
    	bellBtn = new JButton();
    	try {
    		ImageIcon tempIcon = new ImageIcon("bell.png");
    		Image getIconImage = tempIcon.getImage();
    		Image resizeIconImage = getIconImage.getScaledInstance(300, 300, Image.SCALE_AREA_AVERAGING);
    		ImageIcon btnImgIcon = new ImageIcon(resizeIconImage);
    	    bellBtn.setIcon(btnImgIcon);
    	  } catch (Exception ex) {
    	    System.out.println(ex);
    	  }
		bellBtn.addActionListener(listener);
		bellBtn.setBorderPainted(false);
		bellBtn.setBounds(1500, 600, 310, 310);
    	
    	gameLogPanel.add(gameLogScroll);
    	noreumPanel.add(startBtn);
    	noreumPanel.add(howToPlay);
    	noreumPanel.add(howToPlay1);
    	noreumPanel.add(howToPlay2);
    	noreumPanel.add(howToPlay3);
    	noreumPanel.add(posCardP);
    	noreumPanel.add(posCardC);
    	noreumPanel.add(posCardR);
    	noreumFrame.add(noreumPanel);
    	noreumFrame.setVisible(true);
    }
    
    private class EventListeners implements ActionListener, MouseListener, MouseMotionListener
    {
    	//Action Event Listener
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			
    		if (e.getSource() == startBtn) //시작하기 
    		{	
    			gameOver = false;
    	    	playGame();
    		}
    		
    		if (e.getSource() == bellBtn && gameOver == false) 
    		{
    			if (canPressBell == true && isBellPressed == false)
    			{
    				{
    					isBellPressed = true;
    					gameLog.append("\n");
    					gameLog.append("\n");
    					gameLog.append("플레이어가 먼저 종을 쳤습니다!");
    					gameLog.append("\n");
    					if (gameLogNum > 13)
    						gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
    					gameLogNum++;
    				}
        			//플레이어 승리! 카드 가져오기
    				currentPlayer = playerOne;
    				canPressBell = false;
			        collectCards();
    			}
    			else if (canPressBell == true && isBellPressed == true)
    			{
    				//플레이어의 실수! AI의 카드 몰수 
    				{
    					gameLog.append("\n");
    					gameLog.append("\n");
    					gameLog.append("아깝다, 간발의 차였는데!");
    					gameLog.append("\n");
    					if (gameLogNum > 13)
    						gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
    					gameLogNum++;
    				}
    				currentPlayer = playerTwo;
    			}
    			else if (canPressBell == false && isBellPressed == false)
    			{
    				//플레이어의 실수! AI의 카드 몰수 
    	        	if (table.size() < 2) //카드가 없으면 
    	        	{
    	        		if (isBellPressed == false)
    	        		{
    	        			if (madAlice < 10)
    	        			{
    	            			gameLog.append("\n");
    	        				gameLog.append(playerTwo.getName() + ": 카드가 없잖아! 벨 누르지 마!");
    	        				gameLog.append("\n");
    	        				if (gameLogNum > 13)
    	        					gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
    	        				gameLogNum++;
    	        				madAlice++;
    	        			}
    	        			else if (madAlice == 10)
    	        			{
    	        				Timer timer2 = new Timer(1000, null);
    	        		    	timer2.addActionListener((e1) -> {
    	        					gameLog.append("\n");
    	                			gameLog.append("\n");
    	            				gameLog.append(playerTwo.getName() + ": 이런 식으로 할 거면 더 안 할래.");
    	            				gameLog.append("\n");
    	            				if (gameLogNum > 13)
    	            					gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
    	            				gameLogNum++;
    	        					for (int i = 0; i < 3; i++)
    	        					{
    	        						gameLog.append("\n");
    	        						gameLog.append((3 - i) +"");
    	        						gameLog.append("\n");
    	        						try {
    	        							Thread.sleep(1000);
    	        						} catch (InterruptedException e2) {
    	        							e2.printStackTrace();
    	        						}
    	        					}
    	        					System.exit(0); 
    	        		    		timer2.stop();
    	        		    	});
    	        				timer2.start();
    	        			}
    	        		}
    	        		
    	        	}
    	        	else
    	        	{
			        	{
							isBellPressed = true;
							gameLog.append("\n");
							gameLog.append("\n");
							gameLog.append("안타깝네요, 같은 문양이 아니었습니다.");
							gameLog.append("\n");
							if (gameLogNum > 13)
								gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
							gameLogNum++;
						}
						currentPlayer = playerTwo;
				        collectCards();
			        	Timer timer = new Timer(2000, null);
				    	timer.addActionListener((e1) -> {
				        playRounds();
			    		timer.stop();
				    	});
						timer.start();
    	        	}
    			}
    			else if (canPressBell == false && isBellPressed == true)
    			{
    				//대기 시간. 
    				//아무 일도 없음.
    			}
    		}
		}
    	
		
		//Mouse Event Listeners
		
		int xPosPressed, yPosPressed;
    	
		@Override
		public void mousePressed(MouseEvent e) {
			if (isPlayerzTurn == true && topCardP != null && gameOver == false) {
				cardPane.moveToFront(topCardP);
				xPosPressed = e.getX();
				yPosPressed = e.getY();
				if (xPosPressed >= xPosP && xPosPressed <= (xPosP + wCard))
				{
					if (yPosPressed >= yPosP && yPosPressed <= (yPosP + hCard))
					{
						isHolding = true;
					}
				}
			}
		}
	
		@Override
		public void mouseDragged(MouseEvent e) {
			if (topCardP != null && gameOver == false)
			{			
				int xPos, yPos;
				xPos = e.getX() - xPosPressed + xPosP;
				yPos = e.getY() - yPosPressed + yPosP;
				if(isHolding == true)
				{
					topCardP.setBounds(xPos, yPos, wCard, hCard);
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (topCardP != null && gameOver == false)
			{
				int xPos, yPos;
				xPos = e.getX();
				yPos = e.getY();
				xPosPressed = 0;
				yPosPressed = 0;
				if (xPos >= xPosC && xPos <= (xPosC + wCard) && isHolding == true)
				{
					if (yPos >= yPosC && yPos <= (yPosC + hCard))
					{
	        			isHolding = false;
	        			topCardC = topCardP;
	    				topCardC.setBounds(xPosC, yPosC, wCard, hCard);
	    				isPlayerzTurn = false;
	    				continueRound();
					}
				}
				else if (isPlayerzTurn == true)
				{
        			isHolding = false;
					topCardP.setBounds(xPosP, yPosP, wCard, hCard);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
    }
    
    //카드 이미지 담당 
    static class LoadCardImg extends JLabel
    {
		static JLabel cardImg;
		
        public LoadCardImg(String cardImgName)
        {
        	String cardImgNameForSearch = cardImgName + ".png";
    		ImageIcon tempIcon = new ImageIcon(cardImgNameForSearch);
    		
    		Image getIconImage = tempIcon.getImage();
    		Image resizeIconImage = getIconImage.getScaledInstance(wCard, hCard, Image.SCALE_AREA_AVERAGING); //Image.SCALE_SMOOTH
    		ImageIcon cardImgIcon = new ImageIcon(resizeIconImage);

    		cardImg = new JLabel("", cardImgIcon, JLabel.CENTER);
    		cardImg.setLayout(null);
    		cardImg.setBounds(xPosC, yPosC, wCard, hCard);
    		
    		cardPane.add(cardImg);
    		cardPane.moveToFront(cardImg);
        }
        
		public void setImgBounds(int x, int y) { //이미지라고 해도 label의 위치를 수정하는 것 
			cardImg.setBounds(x, y, wCard, hCard);
		}
		
		public void setBoundsR() //AI 카드 위치로 이동 단축 커맨드 
		{
	    	Timer timer = new Timer(delay * 2, null);
	    	timer.addActionListener((e) -> {
				cardImg.setBounds(xPosR, yPosR, wCard, hCard);
				cardImg.setText("");
//				topCardR = cardImg; //AI의 최상위 카드 마크 
    			timer.stop();
	    	});
			timer.start();
		}
		
		public void setBoundsC() //카드 더미 위치로 이동 단축 커맨드 
		{
	    	Timer timer = new Timer(delay * 2, null);
	    	timer.addActionListener((e) -> {
    			cardImg.setBounds(xPosC, yPosC, wCard, hCard);
    			cardImg.setText("");
    			topCardC = cardImg; //가장 최근에 제출된 카드 마크 
    			timer.stop();
	    	});
			timer.start();
		}
		
		public void setBoundsP() //플레이어 카드 위치로 이동 단축 커맨드 
		{
	    	Timer timer = new Timer(delay * 2, null);
	    	timer.addActionListener((e) -> {
    			cardImg.setBounds(xPosP, yPosP, wCard, hCard);
    			cardImg.setText("");
    			topCardP = cardImg; //플레이어의 최상위 카드 마크 
    			timer.stop();
	    	});
			timer.start();
		}
    }
    
    public static void loading() //1초 
    {
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void Log(String log)
    {
    	
    	Timer timer = new Timer(delay, null);
    	timer.addActionListener((e) -> {
        	gameLog.append(log);
        	gameLog.append("\n");
        	if (gameLogNum > 13)
        		gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
        	gameLogNum++;
			timer.stop();
    	});
		timer.start();
    }
    
    
    // Play the simple card game
    public static void playGame() {
		startBtn.setVisible(false);
		noreumPanel.add(bellBtn);
    	noreumPanel.add(gameLogPanel);
		noreumPanel.add(leftCardP);
		noreumPanel.add(leftCardR);
		noreumPanel.add(currentRound);
		noreumPanel.add(currentRoundLabel);
		
		gameLog.append("게임을 시작합니다.");
		gameLog.append("\n");
		if (gameLogNum > 13)
			gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
		gameLogNum++;

		loading();
        shuffleCards(); //카드 셔플 & 카드 이미지 
        dealCards(); 
        
		Timer timer = new Timer(2000, null);
    	timer.addActionListener((e) -> {
            if (playerOne.handSize() == 26 && playerTwo.handSize() == 26)
            	chooseFirstPlayer(); 
    		Timer timer2 = new Timer(2000, null);
	    	timer2.addActionListener((e2) -> {
	            playRounds();
    			timer2.stop();
	    	});
			timer2.start();
			timer.stop();
    	});
		timer.start();
    }

    //카드 셔플 & 이미지 호출
    public static void shuffleCards()
    {
		gameLog.append("\n");
		Timer timer = new Timer(0, null);
    	timer.addActionListener((e) -> {
        	Log("카드를 셔플중입니다.");
    		Timer timer2 = new Timer(1000, null);
	    	timer2.addActionListener((e2) -> {
	    		Log("카드를 셔플했습니다.");
    			timer2.stop();
	    	});
			timer2.start();
			timer.stop();
    	});
		timer.start();
    	
    	tempC1 = new LoadCardImg("noreum_back");
		tempC1.setImgBounds(xPosC, yPosC);
		
		tempR1 = new LoadCardImg("gray_back");
		tempR1.setImgBounds(xPosR, yPosR);
		
		tempR2 = new LoadCardImg("gray_back");
		tempR2.setBoundsR();
		
		tempP1 = new LoadCardImg("gray_back");
		tempP1.setImgBounds(xPosP, yPosP);
		
		tempP2 = new LoadCardImg("gray_back");
		tempP2.setBoundsP();
    }
    
    // Deal 26 cards to each hand in alternating order
    public static void dealCards() { 
        for (int i = 0; i < 26; i++) {
            playerOne.takeCard(deck.deal());
            leftCardP.setText(playerOne.handSize() + "");
            playerTwo.takeCard(deck.deal());
            leftCardR.setText(playerTwo.handSize() + "");
        }
    }

    // Choose who goes first
    public static void chooseFirstPlayer() {
        Random random = new Random();
        int n = random.nextInt(2); //0 or 1

        if (n == 1) { // Make playerTwo the new playerOne
            currentPlayer = playerTwo;
            
    		Log("");
            Log(currentPlayer.getName() + " 선!");
        }
        else
        {
    		Log("");
        	Log(currentPlayer.getName() + " 선!");
        }
    }

    // Play rounds, max 10
    public static void playRounds() {
        if (roundsPlayed < 10 && (gameOver == false)) {
            roundsPlayed++;
			gameLog.append("\n");
			gameLog.append("\n");
        	Log("라운드 " + roundsPlayed);
        	
        	Timer timer = new Timer(1000, null);
	    	timer.addActionListener((e) -> {
            playRound();
    			timer.stop();
	    	});
			timer.start();
        }
        else
        {
        	Timer timer = new Timer(delay * 2, null);
	    	timer.addActionListener((e) -> {
			declareWinner();
    			timer.stop();
	    	});
			timer.start();
        }
    }

    public static void playRound() {
    	if (roundsPlayed == 10)
        	currentRound.setText("마지막");
    	else
        	currentRound.setText(roundsPlayed + "");
    	
        if ((playerOne.handSize() == 52) || (playerTwo.handSize() == 52)) {
            gameOver = true;
        }
        
        isBellPressed = false;
        
    	if (currentPlayer.getName() == playerOne.getName() && gameOver == false)
        {
    		isPlayerzTurn = true;
			gameLog.append("\n");
			gameLog.append("마이 턴!");
			gameLog.append("\n");
            leftCardP.setText(playerOne.handSize() + "");
            leftCardR.setText(playerTwo.handSize() + "");
			if (gameLogNum > 13)
				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
			gameLogNum++;
        }
        else if (currentPlayer.getName() != playerOne.getName() && gameOver == false) 
        {
        	loading();
    		gameLog.append("\n");
			gameLog.append(currentPlayer.getName() + "의 턴!");
			gameLog.append("\n");
            leftCardP.setText(playerOne.handSize() + "");
            leftCardR.setText(playerTwo.handSize() + "");
        	
        	Timer timer = new Timer(delay * 1, null);
	    	timer.addActionListener((e) -> {
			loading();
        	continueRound();
    		timer.stop();
	    	});
			timer.start();
        }
    }
    
    public static void continueRound() {
        boolean suitMatch = false; 
		
		cardToPlay = currentPlayer.playCard();
		tempC1 = new LoadCardImg(cardToPlay.getImgCode());
		tempC1.setBoundsC();
		cardPane.moveToFront(tempC1);
		{
			gameLog.append(currentPlayer.getName() + "가 " + cardToPlay.getName() + "을 제출!");
			gameLog.append("\n");
            leftCardP.setText(playerOne.handSize() + "");
            leftCardR.setText(playerTwo.handSize() + "");
			if (gameLogNum > 13)
				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
			gameLogNum++;
		}
		table.add(cardToPlay);

        suitMatch = checkSuitMatch();

        if (suitMatch == false)
        {
        	Timer timer = new Timer(delay / 2, null);
	    	timer.addActionListener((e) -> {
        	switchCurrentPlayer();
            playRound();
    		timer.stop();
	    	});
			timer.start();
        }
    }

    public static void switchCurrentPlayer() {
        if (currentPlayer == playerOne)
            currentPlayer = playerTwo;
        else if (currentPlayer == playerTwo)
            currentPlayer = playerOne;
    }

    public static boolean checkSuitMatch() {
        int tableSize = table.size();
        int lastSuit;
        int topSuit;

        if (tableSize < 2) {
            return false;
        }
        else {
            lastSuit = table.get(tableSize - 1).getSuit();
            topSuit = table.get(tableSize - 2).getSuit();
        }

        if (lastSuit == topSuit) {
        	canPressBell = true;
        	
        	Timer timer = new Timer(0, null);
	    	timer.addActionListener((e) -> {
			    //시간 내에 정해진 버튼 누르기. 컴퓨터의 AI 이용해 랜덤하게 AI가 선수치는 때도 있음. 방심 금물! 
				//카드가 공개되고 다음 차례로 넘어가기 전 로딩 시간동안 누를 수 있음. 누르지 않고 지나가면 아무 일 없음. 
				//사람같은 AI가 목표. 엄청 늦게 알아차리거나 못 알아채기도 함.
				pressFlag = false;
	        	Random r = new Random();
	            int rand = r.nextInt(20); //0~19
	            if (rand == 0) //못 알아챔 
	            {
	            	Log("");
	            	Log("못 알아챈 모양이다...");
	            }
	            else
	            {
	            	int pressWhen = r.nextInt(6);
			        Timer timer2 = new Timer(1000 + 200 * pressWhen, null);
			    	timer2.addActionListener((e2) -> {
		            	pressFlag = true;
		            	
			        	if (canPressBell == true && pressFlag == true)
			        	{
			        		isBellPressed = true;
			        		pressFlag = false;
							gameLog.append("\n");
							gameLog.append(playerTwo.getName() + "가 버튼을 눌렀다...");
							gameLog.append("\n");
							if (gameLogNum > 13)
								gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
							gameLogNum++;
			        		
			        		if (currentPlayer == playerOne)
							{
								switchCurrentPlayer();
						        collectCards();
							}
							else if (currentPlayer == playerTwo)
							{
						        collectCards();
							}
			        	}
						timer2.stop();
			    	});
					timer2.start();
	            }

		        Timer timer2 = new Timer(2000, null);
		    	timer2.addActionListener((e2) -> {
					canPressBell = false;
			        playRounds();
					timer2.stop();
		    	});
				timer2.start();
	    		timer.stop();
	    	});
			timer.start();
            return true;
        }
        return false;
    }

    // Collect cards from table
    public static void collectCards() {
    	Timer timer = new Timer(800, null);
    	timer.addActionListener((e) -> {
        	if (table.size() > 0) 
        	{
				gameLog.append("\n");
				gameLog.append(currentPlayer.getName() + ", 테이블 위 " + table.size() + "장의 카드 획득!: ");
				gameLog.append("\n");
				if (gameLogNum > 13)
					gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
				gameLogNum++;
		
		        displayTable();
		
		        for (int i = 0; i < table.size(); i++) {
		            Card cardToTake = table.get(i);
		        	Timer timer2 = new Timer(500 * i, null);
			    	timer2.addActionListener((e1) -> {
		                currentPlayer.takeCard(cardToTake);
		                leftCardP.setText(playerOne.handSize() + "");
		                leftCardR.setText(playerTwo.handSize() + "");
		        		timer2.stop();
			    	});
					timer2.start();
            }
            tempC1 = new LoadCardImg("noreum_back");
            table.clear();
        	}
    		timer.stop();
    	});
		timer.start();
    }

    // Displays all the cards currently on the table
    public static void displayTable() {
        for (int i = 0; i < table.size(); i++) {
        	Timer timer = new Timer(500 * i, null);
            if (table.get(i) != null) {
            	gameLog.append("\n");
    			gameLog.append(">>> " + table.get(i).getName());
    			gameLog.append("\n");
    			if (gameLogNum > 13)
    				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
    			gameLogNum++;
    	    	timer.addActionListener((e) -> {
                leftCardP.setText(playerOne.handSize() + "");
                leftCardR.setText(playerTwo.handSize() + "");
        		timer.stop();
    	    });
			timer.start();
            }
        }
        Log("");
    }

    // Declare a winner
    public static void declareWinner() {
        if (playerOne.handSize() > playerTwo.handSize()) { //언제나 1P가 플레이어이도록 수정해둠. 
        	gameLog.append("\n");
			gameLog.append("총 " + playerOne.handSize() + "장의 카드로 " + playerOne.getName() + " 승!");
			gameLog.append("\n");
			if (gameLogNum > 13)
				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
			gameLogNum++;
        }
        else if (playerTwo.handSize() > playerOne.handSize()) {
        	gameLog.append("\n");
			gameLog.append("총 " + playerTwo.handSize() + "장의 카드로 " + playerTwo.getName() + " 승!");
			gameLog.append("\n");
			if (gameLogNum > 13)
				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
			gameLogNum++;;
        }
        else {
        	gameLog.append("\n");
			gameLog.append("세상에 이런 일이! 무승부!");
			gameLog.append("\n");
			if (gameLogNum > 13)
				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
			gameLogNum++;
        }
        Timer timer = new Timer(1000, null);
    	timer.addActionListener((e1) -> {
			gameLog.append("\n");
			gameLog.append("\n");
			gameLog.append("수고하셨습니다. 프로그램이 5초 후 종료됩니다.");
			gameLog.append("\n");
			if (gameLogNum > 13)
				gameLogScroll.getVerticalScrollBar().setValue(gameLogScroll.getVerticalScrollBar().getMaximum());
			gameLogNum++;
			for (int i = 0; i < 5; i++)
			{
				gameLog.append("\n");
				gameLog.append((5 - i) +"");
				gameLog.append("\n");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					gameLog.append("\n");
					gameLog.append("감사합니다.");
				}
			}
        	Timer timer2 = new Timer(6000, null);
	    	timer2.addActionListener((e2) -> {
				System.exit(0); 
        		timer2.stop();
	    	});
			timer2.start();
    		timer.stop();
    	});
		timer.start();
    }
}
