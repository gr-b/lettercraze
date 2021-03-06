package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import controllers.SubmitWordMove;

/**
 *  The highest level entity model.
 * <p>
 * Creation date: (12/15/16)
 * @author Andrew Vanner
 */
public class Model {
	/** Title of level. */
	String title;
	/** Type of level. */
	int type; // 0 - puzzle, 1 - lightning, 2 - theme
	/** Board of the level. */
	Board board;
	/** Score of the level. */
	Score score;
	/** Word list of level. */
	DefaultListModel<String> wordListModel;
	/** Current selected word. */
	Word selectedWord;
	/** Time left to play level. */
	int time;
	/** Highscores of level. */
	int[] highScore;// = {15,30,40,50,60,0,0,0,0,0,0,0,0,0,0};
	/** Tab to access level. */
	int selectedTab = 0; // Used only by player
	/** Checks if level is unlocked. */
	int unlockedLevels = 0;
	/** The last move made in the model. */
	SubmitWordMove lastMove;
	/** The entire previous model. */
	Model lastModel;
	/** List of theme words, only used for theme levels. */
	ArrayList<String> themeWords = new ArrayList<String>();
	/** Turn limit for theme levels. */
	int wordLimit;
	/** Checks if star is unlocked or not. */
	int[] starStatus;
	/** When saving a level from builder. */
	private Integer levelSaverInt;

	/**
	 * Board constructor comment.
	 */
	public Model() {
		this.board = new Board();
		this.wordListModel = new DefaultListModel<String>();
		this.title = "";
		int[] defScore = {0, 0, 0};
		this.score = new Score(defScore);
		this.selectedWord = new Word("", 0);
		this.time = 120;
		//System.out.print("TYPE Is" + this.type);
		this.highScore = new int[15];
		this.lastMove = null;
		this.lastModel = null;
		this.wordLimit = 0;
		this.starStatus = new int[15];
	}
	public int getStarStatus(int index){
		return this.starStatus[index];
	}
	public boolean setStarStatus(int index, int status){
		if(index <15){
			this.starStatus[index] = status;
			return true;
		}
		else{
			return false;
		}
	}
	/** Makes copy of the model to allow moves to be undone. */
	public Model copyModel() {
		Model model = new Model();
		model.setTitle(this.title);
		model.type = new Integer(this.getType());
		model.board = new Board();
		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 6; y++) {
				Tile newTile = new Tile(this.getBoard().getTile(x, y).getLetter());
				newTile.setEnabled(this.getBoard().getTile(x, y).isEnabled());
				newTile.setSelection((this.getBoard().getTile(x, y).isSelected()));
				model.board.setTile(x, y, newTile);
			}
		}

		model.score = new Score(this.getScore().getStarScores());
		model.score.setScoreValue(this.getScore().getScoreValue());
		model.wordListModel = new DefaultListModel<String>();
		for (int x = 0; x < this.wordListModel.size(); x++) {
			model.wordListModel.addElement(this.wordListModel.get(x));
		}
		model.themeWords = new ArrayList<String>();
		model.themeWords.addAll(this.themeWords);

		model.getBoard().setSelectedTileCoords(new Coordinate(this.getBoard().getSelectedTileCoords().x, this.getBoard().getSelectedTileCoords().y));
		model.selectedWord = new Word(this.selectedWord.getWordString(),new Integer(this.selectedWord.getScore()));
		model.time = new Integer(this.getTime());
		for (int i = 0; i < this.getHighScores().length; i++) {
			model.highScore[i] = new Integer(this.getHighScores()[i]);
		}
		model.wordLimit = new Integer(this.getLimit());
		model.selectedTab = this.selectedTab;
		model.unlockedLevels = this.unlockedLevels;
		model.lastModel = this.getLastModel();
		model.setLastMove(this.lastMove);
		this.setLastModel(model);
		return model;
	}
	
	/** Updates stars. */
	public void writeStars(){
		try{
			File outFile = new File("stars.txt");
			outFile.createNewFile();
			FileOutputStream out = new FileOutputStream(outFile, false);
			String s = "";

			for(int i = 0; i < 15; i++){
				s = s + this.starStatus[i] +"\n";

			}
			out.write(s.getBytes());

			out.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	/** Checks start status. */
	public int readStars(){
		FileInputStream in;
		int num = -1; // Error if this returns
		byte[] bA = new byte[40];
		String s = "";
		try {
			in = new FileInputStream("stars.txt");
			for(int i = 0; i < 15;i++){
				in.read(bA);
			}
			s = new String(bA);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] stars = s.split("\n");
		for(int i = 0; i < 15; i++){
			this.starStatus[i] = Integer.parseInt(stars[i]);
		}

		return num;
	}
	/** Updates highscore if applicable. */
	public void writeHighScore(){
		try{
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

	/** Checks the highscore. */
	public int readHighScore(){
		FileInputStream in;
		int num = -1; // Error if this returns
		byte[] bA = new byte[40];
		String s = "";
		try {
			in = new FileInputStream("highscores.txt");
			for(int i = 0; i < 15;i++){
				in.read(bA);
			}
			s = new String(bA);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] scores = s.split("\n");
		for(int i = 0; i < 15; i++){
			this.highScore[i] = Integer.parseInt(scores[i]);
		}

		return num;
	}
	public int[] getHighScores(){
		return this.highScore;
	}

	public boolean setHighScore(int index, int hS){
		if(index <15 && hS >= this.highScore[index]){
			this.highScore[index] = hS;
			this.writeHighScore();
			this.readHighScore();
			return true;
		}
		else 
			return false;
	}

	/** Checks if player can unlock next level */
	public boolean hasWon(){
		return getScore().score >= getScore().getStarScoreIndex(2);
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

	public void addThemeWord(String word) {
		this.themeWords.add(word);
	}

	public ArrayList<String> getThemeWords(){
		return this.themeWords;
	}

	public void setWordListModel(DefaultListModel<String> wordListModel) {
		this.wordListModel = wordListModel;
	}

	public void setLimit(int limit) {
		this.wordLimit = limit;
	}

	public int getLimit() {
		return this.wordLimit;
	}
	public void setLevelSaverInt(Integer selectedItem) {
		this.levelSaverInt = selectedItem;
		
	}
	public Integer getLevelSaverInt() {
		return this.levelSaverInt;
	}
}
