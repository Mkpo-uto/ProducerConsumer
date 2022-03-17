package home;

/*
This class uses the unix code so it does not work on windows native console.
It does work in IntelliJ Idea. These codes must be prepended in the display statement
to work properly in IntelliJ IDEA. 
*/
public class ThreadColor {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
}

