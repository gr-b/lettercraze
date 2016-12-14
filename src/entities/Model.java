package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;

import controllers.SubmitWordMove;

public class Model {

	String title;
	int type; // 0 - puzzle, 1 - lightning, 2 - theme
	Board board;
	Score score;
	DefaultListModel<String> wordListModel;
	Word selectedWord;
	int time;
	/*TODO: Batu - Highscore stuff - make an array, save 15 highscores, read in and out from file */
	int[] highScore = {15,30,40,50,60,0,0,0,0,0,0,0,0,0,0};
	int selectedTab; // Used only by player
	int unlockedLevels = 0;
	SubmitWordMove lastMove;
	Model lastModel;

	public Model() {
		this.board = new Board();
		this.wordListModel = new DefaultListModel<String>();
		this.title = "";
		int[] defScore = {0, 0, 0};
		this.score = new Score(defScore);
		this.selectedWord = new Word("", 0);
		this.time = 120;
		System.out.print("TYPE Is" + this.type);
		this.lastMove = null;
		this.lastModel = null;

	}
	
	public Model copyModel() {
		Model model = new Model();
		model.setTitle(this.title);
		model.type = this.type;
		model.board = new Board();
		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 6; y++) {
				Tile newTile = new Tile(this.getBoard().getTile(x, y).getLetter());
				newTile.setEnabled(this.getBoard().getTile(x, y).isEnabled());
				newTile.setSelection((this.getBoard().getTile(x, y).isSelected()));
				model.board.setTile(x, y, newTile);
			}
		}
		model.score = this.score;
		model.wordListModel = this.wordListModel;
		model.selectedWord = new Word(this.selectedWord.getWordString(),new Integer(this.selectedWord.getScore()));
		model.time = this.time;
		model.highScore = this.highScore;
		model.selectedTab = this.selectedTab;
		model.unlockedLevels = this.unlockedLevels;
		model.lastModel = this.getLastModel();
		model.setLastMove(this.lastMove);
		this.setLastModel(model);
		return this;
	}
	
	//DOESNT't work, will fix it later
	public void writeHighScore(){
		try{
			/*File outFile = new File("highscores.txt");
			outFile.createNewFile();
			FileOutputStream out = new FileOutputStream(outFile, false);
			String str = ""; 
			str = Integer.toString(this.highScore[0]);
			for(int i = 1; i < 15; i++){
				str = str + Integer.toString(this.highScore[i]);
			}			
		out.write(str.getBytes());
		out.close();*/
			File outFile = new File("highscores.txt");
			outFile.createNewFile();
			FileOutputStream out = new FileOutputStream(outFile, false);
			String s = "";
			for(int i = 0; i < 15; i++){
				s = s + this.highScore[i] +"\n";
				
			}
			out.write(s.getBytes());
			
			out.close();
	}
	catch (IOException e){
		e.printStackTrace();
	}
}

public int readHighScore(){
	FileInputStream in;
	int num = -1; // Error if this returns
	byte[] bA = new byte[40];
	String s = "";
	try {
		in = new FileInputStream("highscores.txt");
		for(int i = 0; i < 15;i++){
			in.read(bA);
		//bA[i].getBytes();
		}
		
		s = new String(bA);
		in.close();
		/*in = new FileInputStream("highscores.txt");
		byte[] b = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,};
 		num = in.read();
 		System.out.print("\n BYTE = " + Integer.toString(num));
 		String s ="";
 		for (int i = 0; i < b.length; i++) {
 	        b[i] += '0';
 	        s= s + Byte.toString(b[i]);
 	    }
 		System.out.print("\n STRING = " + s);
 	    int n = Integer.parseInt(s);
		
		System.out.print("\n the byte form of highscores" + n + "\n");*/
		//in.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	//System.out.print("HighScores = " + s );
	String[] scores = s.split("\n");
	for(int i = 0; i < 15; i++){
		this.highScore[i] = Integer.parseInt(scores[i]);
		System.out.println(i + ":" + scores[i]);
	}

	return num;
}
public int[] getHighScores(){
	return this.highScore;
}

public int setHighScore(){
	//do the filePicker stuff
	return -1;
}

public boolean hasWon(){
	return getScore().score > getScore().getStarScoreIndex(0);
}

public Board getBoard(){
	return this.board;
}

public int getType(){
	return this.type;
}

public void setBoard(Board board2) {
	this.board = board2;
}

public void setTitle(String title) {
	this.title = title;
}

public DefaultListModel<String> getWordListModel() {
	return wordListModel;
}

public void addWordListModel(String word) {
	wordListModel.addElement(word);
}

public String getTitle(){
	return this.title;
}

public int removeWordListModel(int index) {
	wordListModel.remove(index);
	int size = wordListModel.getSize();

	if (size == 0) {
		return -1;

	} else {
		if (index == wordListModel.getSize()) {
			index--;
		}
	}
	return index;
}

public Score getScore(){
	return this.score;
}

public void setScore(Score score) {
	this.score = score;
}

public void setScoreValue(int score) {
	this.score.setScoreValue(score);
}

public Word getSelectedWord() {
	return this.selectedWord;
}	

public void setSelectedWord(Word selectedWord) {
	this.selectedWord = selectedWord;
}

public void setType(int index) {
	this.type = index;
}

public void setTime(int time) {
	this.time = time;
}

public int getTime() {
	return this.time;
}

public void setSelectedTab(int tabIndex) {
	this.selectedTab = tabIndex;

}
public int getSelectedIndex() {
	return selectedTab;
}
public boolean hasTitle() {
	return this.title != null;
}
public void setUnlocked(int loadUnlockedNum) {
	this.unlockedLevels = loadUnlockedNum;
}
	
	public void setLastMove(SubmitWordMove move) {
		this.lastMove = move;
	}
	
	public SubmitWordMove getLastMove() {
		return this.lastMove;
	}
	
	public Model getLastModel() {
		return this.lastModel;
	}
	
	public void setLastModel(Model model) {
		this.lastModel = model;
	}

public int getUnlocked() {
	return this.unlockedLevels;
}
}
