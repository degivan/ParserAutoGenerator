package ru.itmo.degtiarenko.translation.autogen;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class OurLexer {
	private InputStream is;
	private int curChar;
	private int curPos;
	private Token curToken;
	private String curString;

	private Map<Token, List<Pattern>> patterns = new HashMap<>();

	{
		List<Pattern> number_Patterns= new ArrayList<>();

		number_Patterns.add(Pattern.compile("[0-9]+"));

		patterns.put(Token.NUMBER, number_Patterns);

		List<Pattern> mul_Patterns= new ArrayList<>();

		mul_Patterns.add(Pattern.compile("\\*"));

		patterns.put(Token.MUL, mul_Patterns);

		List<Pattern> open_bracket_Patterns= new ArrayList<>();

		open_bracket_Patterns.add(Pattern.compile("\\("));

		patterns.put(Token.OPEN_BRACKET, open_bracket_Patterns);

		List<Pattern> eps_Patterns= new ArrayList<>();


		patterns.put(Token.EPS, eps_Patterns);

		List<Pattern> close_bracket_Patterns= new ArrayList<>();

		close_bracket_Patterns.add(Pattern.compile("\\)"));

		patterns.put(Token.CLOSE_BRACKET, close_bracket_Patterns);

		List<Pattern> ws_Patterns= new ArrayList<>();

		ws_Patterns.add(Pattern.compile(" "));

		patterns.put(Token.WS, ws_Patterns);

		List<Pattern> eof_Patterns= new ArrayList<>();


		patterns.put(Token.EOF, eof_Patterns);

		List<Pattern> plus_Patterns= new ArrayList<>();

		plus_Patterns.add(Pattern.compile("-"));

		patterns.put(Token.PLUS, plus_Patterns);


	}
	public OurLexer(InputStream is) throws ParseException, IOException {
		this.is = is;
		curPos = 0;
		nextChar();
	}

	private boolean isBlank(int c) { return c == ' ' || c == '\r' || c == '\n' || c == '\t'; }

	private void nextChar() throws ParseException, IOException {
		curPos++;
		try {
			curChar = is.read();
		} catch (IOException e) {
			throw new ParseException(e.getMessage(), curPos);
		}
	}

	public Token curToken() {
		return curToken;
	}

	public int curPos() {
		return curPos;
	}

	public String curString() {
		return curString;
	}

	public void nextToken() throws ParseException, IOException {
		curString = "";
		if (curChar == -1) {
			curToken = Token.EOF;
			return;
		}
		boolean maxPatternFlag = false;
		boolean skipFlag = false;
		boolean returnFlag = false;
		for(int i = 0 ; i < 1000; i++) {

			curString += (char) curChar;
			if (patterns.get(Token.EPS).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.EPS;

				returnFlag = true;
			}
			else if (patterns.get(Token.WS).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.WS;
				skipFlag = true;
				returnFlag = true;
			}
			else if (patterns.get(Token.OPEN_BRACKET).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.OPEN_BRACKET;

				returnFlag = true;
			}
			else if (patterns.get(Token.CLOSE_BRACKET).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.CLOSE_BRACKET;

				returnFlag = true;
			}
			else if (patterns.get(Token.PLUS).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.PLUS;

				returnFlag = true;
			}
			else if (patterns.get(Token.MUL).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.MUL;

				returnFlag = true;
			}
			else if (patterns.get(Token.NUMBER).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.NUMBER;

				returnFlag = true;
			}
			else if (patterns.get(Token.EOF).stream().anyMatch(p -> p.matcher(curString).matches())) {
				curToken = Token.EOF;

				returnFlag = true;
			}
			else if(returnFlag) {maxPatternFlag = true;}
			if (returnFlag && maxPatternFlag) { curString = curString.substring(0, curString.length() - 1);return;}
			nextChar();
			if(skipFlag) {nextToken();}
		}
		throw new AssertionError();
	}
}
