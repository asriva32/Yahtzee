/*
Arnav Srivastava and Adithya Sridhar
pd1
In the yahtzee class we created the entirety of the game. The Yahtzee class implements both the ActionListener and ItemListener methods. It creates JLabel images for dice, JTable for the score choices, another JTable for the score card, and JButton/JCheckBoxes to roll and select the dice. In the ActionListener method we created three seperate outcomes depending on what was clicked. If one of the options for the JComboBox was clicked we added the score to the score card and switched to the next player. If the JButton to rolldice was clicked the dice would roll and possible scores would be put onto the JTable in accordance to the dice values. Finally, if the start button was clicked on the rules JPanel it would switch to the game panel. We also used JCheckBoxes to hold the dice and switch to the next player. Once you click the turnover JCheckBox you can select your option. You can click the JCheckBoxes underneath each dice to hold them, and unselect them to unhold them.
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
class Yahtzee implements ActionListener, ItemListener{
  //GUI Objects
  JFrame frame;
  private int yatb1 = 0;
  private int yatb2 = 0;
  private int yat1cnt = 0;
  private int yat2cnt = 0;
  private int step = 1;
  private int prevStep = 1;
  private int prevPos = -1;
  private int numRolls = 0;
  private boolean good = true;
  private boolean[] pl1 = new boolean[13];
  private boolean[] pl2 = new boolean[13];
  // keeps track of player 1 and player 2 scores
  private ArrayList<Integer> p1score = new ArrayList<Integer>();
  private ArrayList<Integer> p2score = new ArrayList<Integer>();
  // keeps track of whether or not the dice are selected to being held
  private boolean [] selected = new boolean[5]; 
  private boolean done = false;
  JButton rollDice, start;
  // dice checkboxes
  JCheckBox d1, d2, d3, d4, d5, turnOver;
  JLabel currentPlayer, currentTurn;
  JLabel [] diceLabels;
  JPanel dicePane, rules, gameOver;
  JLabel gameRules1,gameRules2,gameRules3, winner;
  String [][] scoreData;
  // keeps track of potential scores
  private int[] finalVal = new int[13];
  JComboBox choice;
  // JTable for the score card as well as the options
  JTable scoreCard, potentialChoices;
  private int [] diceVals = new int[5];
  private int[] prevDiceVals = new int[5];
  // used in JComboBox
  private String[] diceGifs = {"dice1.gif", "dice2.gif", "dice3.gif", "dice4.gif", "dice5.gif", "dice6.gif"};
  private String[] choices = {"1. Ones", "2. Twos", "3. Threes", "4. Fours", "5. Fives", "6. Sixes", "7. Three of a Kind", "8. Four of a Kind", "9. Full House", "10. Low Straight", "11. High Straight", "12. Yahtzee", "13. Chance", "14. Yahtzee Bonus"};
  // used to create JTable
  private String[] ops = {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Upper Bonus", "Three of a Kind", "Four of a Kind", "Full House", "Low Straight", "High Straight", "Yahtzee", "Chance", "Yahtzee Bonus"};
  private String[] fs = {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Three of a Kind", "Four of a Kind", "Full House", "Low Straight", "High Straight", "Yahtzee", "Chance", "Yahtzee Bonus"};
  ArrayList<String> list = new ArrayList<String>();

  public Yahtzee() {

    //Turns on GUI Functions with Naming
    JFrame.setDefaultLookAndFeelDecorated(true); 
    frame = new JFrame("Yahztee");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Initializes Dice Pane
    dicePane = new JPanel();
    dicePane.setLayout(null);
    //Initializes Rule JPanel
    startScreen(); 
    //Initializes gameOver JPanel
    gameOver = new JPanel();
    gameOver.setLayout(new BorderLayout());
    gameOver.setBackground(Color.ORANGE);
    winner = new JLabel();
    Dimension pl = winner.getPreferredSize(); winner.setBounds(250,250,pl.width,pl.height);
    gameOver.add(winner);
    //Creates Dice Images
    diceLabels = new JLabel[5];
    for(int i = 0; i < 5; i++) {
      diceLabels[i] = new JLabel(new ImageIcon(diceGifs[i]));
      Dimension sz = diceLabels[i].getPreferredSize();     
      diceLabels[i].setBounds(2 + (i*50), 85, (sz.width), (sz.height));
      diceLabels[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
      dicePane.add(diceLabels[i]);
    }

    rollDice = new JButton("Roll Dice");
    rollDice.setActionCommand("Roll Dice");
    rollDice.addActionListener(this);
    Dimension size = rollDice.getPreferredSize();
    rollDice.setBounds(75, 160, size.width, size.height);
    dicePane.add(rollDice);
    
    dicePane.setBackground(Color.BLUE);

    
    // create current player label
    currentPlayer = new JLabel("Player 1 is playing.");
    Dimension pl1 = currentPlayer.getPreferredSize();     
  currentPlayer.setBounds(70, 20, (pl1.width+30), (pl1.height+30));
    dicePane.add(currentPlayer);

    
    // create turns left label
    currentTurn = new JLabel("3 Turns Left.");
    Dimension pl2 = currentTurn.getPreferredSize();     
    currentTurn.setBounds(70, 200, (pl2.width+50), (pl2.height)); 
    dicePane.add(currentTurn);

    
    // Create JComboBox
    choice = new JComboBox(choices);
    Dimension cbox = choice.getPreferredSize();
    choice.setBounds(300, 0, (cbox.width-20), (cbox.height));
    choice.setSelectedIndex(0);
    choice.addActionListener(this);
    dicePane.add(choice);
    list.add("1. Ones");
    list.add("2. Twos");
    list.add("3. Threes");
    list.add("4. Fours");
    list.add("5. Fives");
    list.add("6. Sixes");
    list.add("7. Three of a Kind");
    list.add("8. Four of a Kind");
    list.add("9. Full House");
    list.add("10. Low Straight");
    list.add("11. High Straight");
    list.add("12. Yahtzee");
    list.add("13. Chance");
    list.add("14. Yahtzee Bonus");
    // create checkboxes for dice
    d1 = new JCheckBox("d1");
    Dimension one = d1.getPreferredSize();
    d1.setBounds(2, 130, one.width, one.height);
    d1.addItemListener(this);
    d2 = new JCheckBox("d2");
    Dimension two = d2.getPreferredSize();
    d2.setBounds(49, 130, one.width, one.height);
    d2.addItemListener(this);
    d3 = new JCheckBox("d3");
    Dimension three = d3.getPreferredSize();
    d3.setBounds(96, 130, one.width, one.height);
    d3.addItemListener(this);
    d4 = new JCheckBox("d4");
    Dimension four = d4.getPreferredSize();
    d4.setBounds(143, 130, one.width, one.height);
    d4.addItemListener(this);
    d5 = new JCheckBox("d5");
    Dimension five = d5.getPreferredSize();
    d5.setBounds(190, 130, one.width, one.height);
    d5.addItemListener(this);
    turnOver = new JCheckBox("Turn over?");
    Dimension six = turnOver.getPreferredSize();
    turnOver.setBounds(65, 240, one.width+70, one.height); 
    turnOver.addItemListener(this);
    // add checkboxes to the the JPanel
    dicePane.add(d1);
    dicePane.add(d2);
    dicePane.add(d3);
    dicePane.add(d4);
    dicePane.add(d5);
    dicePane.add(turnOver);
    // creates the score card
    String[] titles = {" ", "Player 1", "Player 2"};
    scoreData = new String[15][3];
    for(int i = 0; i < 15;i++){
      scoreData[i][0] = ops[i];
    }
    scoreCard = new JTable(scoreData, titles); 
    scoreCard.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    scoreCard.setLayout(new GridLayout(1,1));
    JScrollPane sp = new JScrollPane(scoreCard);
    Dimension score = sp.getPreferredSize();
    sp.setBounds(250, 25, 250, score.height-140);
    dicePane.add(sp);
    // creates the score option JTable
    String [] title = {" ", "Choices"};
    String [][] scoreChoices = new String[14][2];
    for(int i = 0; i < 14;i++){
      scoreChoices[i][0] = fs[i];
    }
    potentialChoices = new JTable(scoreChoices, title);
    JScrollPane scrollPane = new JScrollPane(potentialChoices);
    Dimension s = scrollPane.getPreferredSize();
    scrollPane.setBounds(520, 25, 220,s.height-155);
    dicePane.add(scrollPane);
    // set all values of player 2 and player 1 to 0
    for(int i = 0; i < 14;i++){
      p1score.add(0);
    }
    for(int i = 0; i < 14;i++){
      p2score.add(0);
    }
    // add rules to jFrame
    frame.add(rules);
    frame.setSize(500, 500);
    frame.pack();
    frame.setVisible(true);
    
  }

  public void startScreen() {
    rules = new JPanel();
    rules.setBackground(Color.GRAY);
    rules.setLayout(new GridLayout(4,1));
    gameRules1 = new JLabel("<html> 1. Before choosing an option click the turnover check box then <br/> make your choice. Uncheck the turnover checkbox before <br/> proceeding to the next player.</html>");
    gameRules1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    gameRules2 = new JLabel("<html> 2. You can hold the dice by clicking the checkboxes underneath each <br/> dice, all dice that are selected will not be rolled.</html>");
    gameRules2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    gameRules3 = new JLabel("3.Your choices will be displayed on the score card.");
    gameRules3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    start = new JButton("START");
    start.setActionCommand("start");
    start.addActionListener(this);
    rules.add(gameRules1);
    rules.add(gameRules2);
    rules.add(gameRules3);
    rules.add(start);
  }
  // the itemStateChanged method checks whether or not a checkbox is selected
  public void itemStateChanged(ItemEvent e){
      if(e.getSource() == turnOver){
        if(e.getStateChange()==1){
          // if the turnOver checkbox is selected then player1's turn is over and it swaps to player 2
          done = true;
          step++;
        }else{
          done = false;
          if(step%2!=0){
            currentPlayer.setText("Player 1 is playing.");
          }else{
            currentPlayer.setText("Player 2 is playing.");
          }
        }
      }
    // changes the selected array depending on whether or not each dice is selected to hold
      if(e.getSource() == d1){
        if(e.getStateChange() == 1){
          selected[0] = true;
        }else{
          selected[0] = false;
        }
      }else if(e.getSource() == d2){
        if(e.getStateChange() == 1){
          selected[1] = true;
        }else{
          selected[1] = false;
        }
      }else if(e.getSource() == d3){
        if(e.getStateChange() == 1){
          selected[2] = true;
        }else{
          selected[2] = false;
        }
      }else if(e.getSource() == d4){
        if(e.getStateChange() == 1){
          selected[3] = true;
        }else{
          selected[3] = false;
        }
      }else{
        if(e.getStateChange() == 1){
          selected[4] = true;
        }else{
          selected[4] = false;
        }
      }
  }
  
  public void actionPerformed(ActionEvent event){
    Object source = event.getSource();
    String eventName = event.getActionCommand();
    Random rand = new Random();
    // changes the rules panel to the game panel
    if(eventName.equals("start")){
      frame.remove(rules);
      frame.add(dicePane);
      frame.revalidate();
      frame.repaint();
      frame.pack();
    // randomizes the dice and provides a call to the potential value method, does not work if player does greater than 3 rolls
    }else if(numRolls < 3 && source instanceof JButton){
    numRolls++;
    currentTurn.setText(3-numRolls + " rolls left.");  
    rolls(numRolls);
    // randomizes dice values
    for(int i = 0; i < 5; i++){
      if(selected[i]==false){
      diceVals[i] = (rand.nextInt(6) + 1);
      prevDiceVals[i] = diceVals[i];
      diceLabels[i].setIcon(new ImageIcon(diceGifs[diceVals[i]-1]));
      }else{
      diceVals[i] = prevDiceVals[i];
      diceLabels[i].setIcon(new ImageIcon(diceGifs[diceVals[i]-1]));
      }
    }
  // find potential values
  potentialVal(diceVals);
  // holds a switch statement for each option
  }else if(source instanceof JComboBox){
    JComboBox cbox = (JComboBox)event.getSource();
    int index = (int) (list.indexOf(cbox.getSelectedItem())) + 1;
    switch(index) {
      case 1: 
        // if step is even that means that it is player1's turn
        // comments would be repetitive so I only commented the first case
        if(good && step%2==0){
        // does not work if option has already been picked
        boolean picked = alreadyPicked(pl1[0]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        // if player decides to switch what option they choose during their turn the previous value is reset and the new one is inputted
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        // resets the number of rolls
        numRolls = 0;
        pl1[0] = true;
        // adds value to score arraylist
        p1score.set(0, finalVal[0]);
        // updates the scorecard JTable
        updateScore(String.valueOf(finalVal[0]), 0);
        prevStep = step;
        prevPos = 0;
        }
        // if step is odd that means it is player 2's turn
        else if(good){
        boolean picked = alreadyPicked(pl2[0]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[0] = true;
        p2score.set(0,finalVal[0]);
        updateScore(String.valueOf(finalVal[0]), 0);
        prevStep = step;
        prevPos = 0;
        }
        break;
      case 2:
        if(good && step%2==0) {
          boolean picked = alreadyPicked(pl1[1]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0; 
        pl1[1] = true;
        p1score.set(1, finalVal[1]);
        updateScore(String.valueOf(finalVal[1]), 1);
        prevStep = step;
        prevPos = 1;
        } else if(good){
        boolean picked = alreadyPicked(pl2[1]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[1] = true;
        p2score.set(1, finalVal[1]);
        updateScore(String.valueOf(finalVal[1]), 1);
        prevStep = step;
        prevPos = 1;
        }
        break;
      case 3:
       if(good && (step % 2)==0) {
         boolean picked = alreadyPicked(pl1[2]);
         if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
       numRolls = 0;
       pl1[2] = true;
       p1score.set(2, finalVal[2]);
       updateScore(String.valueOf(finalVal[2]), 2);
        prevStep = step;
        prevPos = 2;
        } else if(good){
         boolean picked = alreadyPicked(pl2[2]);
         if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
         numRolls = 0;
         pl2[2] = true;
         p2score.set(2, finalVal[2]);
         updateScore(String.valueOf(finalVal[2]), 2);
        prevStep = step;
        prevPos = 2;
        }
        break;
      case 4:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[3]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[3] = true;
        p1score.set(3, finalVal[3]);
        updateScore(String.valueOf(finalVal[3]), 3);
        prevStep = step;
        prevPos = 3;
        } else if (good){
        boolean picked = alreadyPicked(pl2[3]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[3] = true;
        p2score.set(3, finalVal[3]);
        updateScore(String.valueOf(finalVal[3]), 3);
        prevStep = step;
        prevPos = 3;
        }
        break;
      case 5:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[4]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[4] = true;
        p1score.set(4, finalVal[4]);
        updateScore(String.valueOf(finalVal[4]), 4);
        prevStep = step;
        prevPos = 4;
        } else if(good){
          boolean picked = alreadyPicked(pl2[4]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
          numRolls = 0;
          pl2[4] = true;
          p2score.set(4, finalVal[4]);
          updateScore(String.valueOf(finalVal[4]), 4);
        prevStep = step;
        prevPos = 4;
        }
        break;
      case 6:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[5]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
       if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[5] = true;
        p1score.set(5, finalVal[5]);
        updateScore(String.valueOf(finalVal[5]), 5);
        prevStep = step;
        prevPos = 5;  
        } else if(good){
        boolean picked = alreadyPicked(pl2[5]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[5] = true;
        p2score.set(5, finalVal[5]);
        updateScore(String.valueOf(finalVal[5]), 5);
        prevStep = step;
        prevPos = 5;
        }
        break;
      case 7:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[6]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[6] = true;
        p1score.set(6,finalVal[6]);
        updateScore(String.valueOf(finalVal[6]), 7);
        prevStep = step;
        prevPos = 7;
        } else if(good){
        boolean picked = alreadyPicked(pl2[6]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
      if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[6] = true;
        p2score.set(6, finalVal[6]);
        updateScore(String.valueOf(finalVal[6]), 7);
        prevStep = step;
        prevPos = 7;
        }
        break;
      case 8:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[7]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
          numRolls = 0;
          pl1[7] = true;
          p1score.set(7, finalVal[7]);
          updateScore(String.valueOf(finalVal[7]), 8);
        prevStep = step;
        prevPos = 8;
        } else if(good){
        boolean picked = alreadyPicked(pl2[7]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[7] = true;
        p2score.set(7, finalVal[7]);
        updateScore(String.valueOf(finalVal[7]), 8);
        prevStep = step;
        prevPos = 8;
        }
        break;
      case 9:
        if(good && step%2==0) {
          boolean picked = alreadyPicked(pl1[8]);
          if(picked){
            System.out.println("Pick Another Option.");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
          numRolls = 0;
          pl1[8] = true;
          p1score.set(8, finalVal[8]);
          updateScore(String.valueOf(finalVal[8]), 9);
        prevStep = step;
        prevPos = 9;
        } else if(good){
          boolean picked = alreadyPicked(pl2[8]);
          if(picked){
            System.out.println("Pick Another Option.");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
          numRolls = 0;
          pl2[8] = true;
          p2score.set(8, finalVal[8]);
          updateScore(String.valueOf(finalVal[8]), 9);
        prevStep = step;
        prevPos = 9;
        }
        break;
      case 10:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[9]);
        if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[9] = true;
        p1score.set(9, finalVal[9]);
        updateScore(String.valueOf(finalVal[9]), 10);
        prevStep = step;
        prevPos = 10;
        } else if(good){
        boolean picked = alreadyPicked(pl2[9]);
        if(picked){
            System.out.println("Pick another option");
            break;
        }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[9] = true;
        p2score.set(9, finalVal[9]);
        updateScore(String.valueOf(finalVal[9]), 10);
        prevStep = step;
        prevPos = 10;
        }
      break;
      case 11:
        if(good && (step % 2)==0) {
          boolean picked = alreadyPicked(pl1[10]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
          numRolls = 0;
          pl1[10] = true;
          p1score.set(10, finalVal[10]);
          updateScore(String.valueOf(finalVal[10]), 11);
          prevStep = step;
          prevPos = 11;
        } else if(good){
          boolean picked = alreadyPicked(pl2[10]);
          if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
          numRolls = 0;
          pl2[10] = true;
          p2score.set(10, finalVal[10]);
          updateScore(String.valueOf(finalVal[10]), 11);
          prevStep = step;
          prevPos = 11;
        }
        break;
      case 12:
        if(good && (step % 2)==0) {
        yat1cnt++;
        if(yat1cnt==1){
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[11] = true;
        p1score.set(11, finalVal[11]);
        updateScore(String.valueOf(finalVal[11]), 12);
        }
        prevStep = step;
        prevPos = 12;
        } else if(good){
        yat2cnt++;
        if(yat2cnt==1){
       if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[11] = true;
        p2score.set(11, finalVal[11]);
        updateScore(String.valueOf(finalVal[11]), 12);
        }
        prevStep = step;
        prevPos = 12;
        } 
        break;
      case 13:
        if(good && (step % 2)==0){
          boolean picked = alreadyPicked(pl1[12]);
        if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl1[prevPos] = false;
        }
        numRolls = 0;
        pl1[12] = true;  
        p1score.set(12, finalVal[12]);
        updateScore(String.valueOf(finalVal[12]), 13);
        prevStep = step;
        prevPos = 13;
        } else if(good){
        boolean picked = alreadyPicked(pl2[12]);
        if(picked){
            System.out.println("Pick another option");
            break;
          }
        if(checkPrevious()){
        updateScore("", prevPos);
        pl2[prevPos] = false;
        }
        numRolls = 0;
        pl2[12] = true;
        p2score.set(12, finalVal[12]);
        updateScore(String.valueOf(finalVal[12]), 13);
        prevStep = step;
        prevPos = 13;
        }
        break;
      case 14:
        // if the yahtzee option has been chosen the player is allowed to choose the yahtzee bonus option
        if(good && (step % 2)==0 && pl1[11]) {
        if(checkPrevious()){
          updateScore("", prevPos);
          pl1[prevPos] = false;
        }
          numRolls = 0;
          String u = (String)scoreCard.getValueAt(12, 1);
          int x = Integer.parseInt(u);
          if(yat1cnt>1 && x == 50 && finalVal[11] == 50){
          yatb1 += 100;
          }
          p1score.set(13, yatb1);
          updateScore(String.valueOf(yatb1), 14);
          prevStep = 14;
        } else if(pl2[11] && good){
        if(checkPrevious()){
          updateScore("", prevPos);
          pl2[prevPos] = false;
        }
          String u = (String)scoreCard.getValueAt(12, 2);
          int x = Integer.parseInt(u);
          if(yat2cnt>1 && x == 50 && finalVal[11] == 50){
          yatb2 += 100;
          }
          numRolls = 0;
          p2score.set(13, yatb2);
          updateScore(String.valueOf(yatb2),14);
          prevStep = 14;
        }
        break;
    }
    currentTurn.setText("3 rolls left.");
    // check if upper score is greater than 63
      if(checkUpperScore()){
        updateScore("35", 6);
      }
    // checks if all options have been chosen by both players
    if(gameFinished(pl1,pl2)){
      // remves game panel and adds the game over panel
      frame.remove(dicePane);
      frame.add(gameOver);
      frame.revalidate();
      frame.repaint();
      frame.pack();
      winner.setText(whoisWinning() + " Wins!");
    }
  }
  }
  // creates the values for each option based on the dice values
  public void potentialVal(int[]diceVals) {
    // creates a frequency array
    int[] count = new int[7];
    for(int i = 0; i < 13;i++) finalVal[i] = 0;
    
    for(int i=0; i < diceVals.length; i++) {
      count[diceVals[i]]++;
    }
    // sets the first 6 options which are ones-sixes
    finalVal[0] = (count[1]);
    finalVal[1] = (count[2] * 2);
    finalVal[2] = (count[3] * 3);
    finalVal[3] = (count[4] * 4);
    finalVal[4] = (count[5] * 5);
    finalVal[5] = (count[6] * 6);
    int cnt2 = 0;
    int cnt3 = 0;
    int cnt4 = 0;
    int yat = 0;
    // sees if their are repetitive dice values
    for(int i = 0; i < count.length;i++){
      // if 4 dice are the same than it is four of a kind
      if(count[i] >= 4){
        cnt4++;
      }
      // if all 5 dice are the same than it is a yahtzee
      if(count[i] == 5){
        yat++;
      }
      // if three dice are the same than it is three of a kind
      if(count[i] >= 3){
        cnt3++;
      }
      if(count[i] == 2){
        cnt2++;
      }
    }
    int sum = 0;
    for(int i = 0; i < diceVals.length;i++){
      sum += diceVals[i];
    }
    finalVal[12] = sum;
    // sets the value to each of the options that have repetitive dice values
    if(cnt2 > 0 && cnt3 > 0){
      finalVal[8] = 25;
    }
    if(cnt3>0){
      finalVal[6] = sum;
    }
    if(cnt4>0){
      finalVal[7] = sum;
    }
    if(yat>0){
      finalVal[11] = 50;
    }
    // checks if there is a low straight or a high straight
    boolean lowStraight = false;
    boolean highStraight = false;
    Arrays.sort(diceVals);
    ArrayList<Integer> dice = new ArrayList<Integer>();
    for(int i = 0; i < diceVals.length;i++){
      dice.add(diceVals[i]);
    }
    for(int i = 0; i < dice.size()-1;i++){
      if(dice.get(i) == dice.get(i+1)){
        dice.remove(i);
      }
    }
    for(int i = 3; i < dice.size();i++){
      if(dice.get(i) - dice.get(i-1) == 1 && dice.get(i-1) - dice.get(i-2) == 1 && dice.get(i-2) - dice.get(i-3) == 1){
        lowStraight = true;
        break;
      }
    }
    if(diceVals[4] - diceVals[3] == 1 && diceVals[3] - diceVals[2] == 1 && diceVals[2] - diceVals[1] == 1 && diceVals[1] - diceVals[0] == 1){
        highStraight = true;
      }
    // sets the value if the dice values are either a low or high straight
    if(lowStraight){
      finalVal[9] = 30;
    }
    if(highStraight){
      finalVal[10] = 40;
    }
    // updates the option JTable
    for(int i = 0; i < finalVal.length;i++) {
      updateChoice(String.valueOf(finalVal[i]), i);
    }
    if(step%2!=0){
      updateChoice(String.valueOf(yatb1), 13);
    }else{
      updateChoice(String.valueOf(yatb2), 13);
    }
  }
// finds the sum of the p1score or p2score array
private int arraySum(ArrayList<Integer> input) {
    int sum = 0;
    int upperSection = 0;
    for(int i = 0; i < 6;i++){
      upperSection += input.get(i);
    }
    for(int i = 0; i < input.size();i++){
      sum += input.get(i);
    }
  // calculates upper section bonus
    if(upperSection >= 63){
      sum += 35;
    }
    return sum;
  }
// checks if there are more than 3 rolls
private void rolls(int rollCount){
  if(rollCount > 3){
    good = false;
  }else{
    good = true;
  }
}
// checks if the option has already been chosen
private boolean alreadyPicked(boolean val){
  if(val==true){
    return true;
  }else{
    return false;
  }
}
// checks if all players have chosen all options
private boolean gameFinished(boolean[]pl1, boolean[]pl2){
  for(int i = 0; i < pl1.length-1;i++){
    if(pl1[i]==false || pl2[i]==false){
      return false;
    }
  }
  return true;
}
// checks to see if the game is still on the same player's turn
public boolean checkPrevious(){
  if(prevStep == step && prevStep!= 14){
    return true;
    }
    return false;
  }
  // updates the score card JTable
  public void updateScore(String value, int pos) {
    if(step%2==0){
      scoreCard.setValueAt(value, pos, 1);
    }else{
      scoreCard.setValueAt(value, pos, 2);
    }
  }
  // updates the options JTable
  public void updateChoice(String value, int pos) {
      potentialChoices.setValueAt(value, pos, 1);
  }
  public boolean checkUpperScore(){
    int upperSection = 0;
    if(step%2==0){
    for(int i = 0; i < 6;i++){
      upperSection += p1score.get(i);
    }
    }else{
      for(int i = 0; i < 6;i++){
      upperSection += p2score.get(i);
    }
    }
    if(upperSection >= 63){
      return true;
    }
    return false;
  }
  // checks to see who has a higher score
  public String whoisWinning() {
    if(arraySum(p1score) > arraySum(p2score)){
      return "Player 1";
    }else if (arraySum(p2score) > arraySum(p1score)){
      return "Player 2";
    }else{
      return "No One";
    }
  }
}