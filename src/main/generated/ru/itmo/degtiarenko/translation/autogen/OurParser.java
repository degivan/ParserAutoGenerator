package ru.itmo.degtiarenko.translation.autogen;

import java.io.InputStream;
import java.text.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;
public class OurParser {
	private OurLexer lex;
	public Integer parse(InputStream is) throws ParseException, IOException {
		lex = new OurLexer(is);
		lex.nextToken();
		return start();
	}

	private Integer t() throws ParseException, IOException {
		switch (lex.curToken()) {
			case NUMBER: {
				List<Integer> f = new ArrayList<>();
				List<Integer> t1 = new ArrayList<>();
				f.add(f());
				t1.add(t1(((Supplier<Integer>) () ->{return f.get(0);}).get()));
				{return t1.get(0);}
			}
			case OPEN_BRACKET: {
				List<Integer> f = new ArrayList<>();
				List<Integer> t1 = new ArrayList<>();
				f.add(f());
				t1.add(t1(((Supplier<Integer>) () ->{return f.get(0);}).get()));
				{return t1.get(0);}
			}
			default:
				throw new AssertionError();
		}
	}

	private Integer f() throws ParseException, IOException {
		switch (lex.curToken()) {
			case NUMBER: {
				List<String> NUMBER = new ArrayList<>();
				if (lex.curToken().toString().equals("NUMBER")) {
					NUMBER.add(lex.curString());
				} else {
					throw new AssertionError("NUMBER expected, instead of " + lex.curToken().toString());
				}
				lex.nextToken();
				{return Integer.valueOf(NUMBER.get(0));}
			}
			case OPEN_BRACKET: {
				List<String> OPEN_BRACKET = new ArrayList<>();
				List<Integer> start = new ArrayList<>();
				List<String> CLOSE_BRACKET = new ArrayList<>();
				if (lex.curToken().toString().equals("OPEN_BRACKET")) {
					OPEN_BRACKET.add(lex.curString());
				} else {
					throw new AssertionError("OPEN_BRACKET expected, instead of " + lex.curToken().toString());
				}
				lex.nextToken();
				start.add(start());
				if (lex.curToken().toString().equals("CLOSE_BRACKET")) {
					CLOSE_BRACKET.add(lex.curString());
				} else {
					throw new AssertionError("CLOSE_BRACKET expected, instead of " + lex.curToken().toString());
				}
				lex.nextToken();
				{return start.get(0);}
			}
			default:
				throw new AssertionError();
		}
	}

	private Integer start() throws ParseException, IOException {
		switch (lex.curToken()) {
			case NUMBER: {
				List<Integer> t = new ArrayList<>();
				List<Integer> e1 = new ArrayList<>();
				t.add(t());
				e1.add(e1(((Supplier<Integer>) () ->{return t.get(0);}).get()));
				{return e1.get(0);}
			}
			case OPEN_BRACKET: {
				List<Integer> t = new ArrayList<>();
				List<Integer> e1 = new ArrayList<>();
				t.add(t());
				e1.add(e1(((Supplier<Integer>) () ->{return t.get(0);}).get()));
				{return e1.get(0);}
			}
			default:
				throw new AssertionError();
		}
	}

	private Integer e1(Integer acc) throws ParseException, IOException {
		switch (lex.curToken()) {
			case CLOSE_BRACKET: {
				{return acc;}
			}
			case EOF: {
				{return acc;}
			}
			case PLUS: {
				List<String> PLUS = new ArrayList<>();
				List<Integer> t = new ArrayList<>();
				List<Integer> e1 = new ArrayList<>();
				if (lex.curToken().toString().equals("PLUS")) {
					PLUS.add(lex.curString());
				} else {
					throw new AssertionError("PLUS expected, instead of " + lex.curToken().toString());
				}
				lex.nextToken();
				t.add(t());
				e1.add(e1(((Supplier<Integer>) () ->{return acc - t.get(0);}).get()));
				{return e1.get(0);}
			}
			default:
				throw new AssertionError();
		}
	}

	private Integer t1(Integer acc) throws ParseException, IOException {
		switch (lex.curToken()) {
			case MUL: {
				List<String> MUL = new ArrayList<>();
				List<Integer> f = new ArrayList<>();
				List<Integer> t1 = new ArrayList<>();
				if (lex.curToken().toString().equals("MUL")) {
					MUL.add(lex.curString());
				} else {
					throw new AssertionError("MUL expected, instead of " + lex.curToken().toString());
				}
				lex.nextToken();
				f.add(f());
				t1.add(t1(((Supplier<Integer>) () ->{return f.get(0) * acc;}).get()));
				{return t1.get(0);}
			}
			case CLOSE_BRACKET: {
				{return acc;}
			}
			case EOF: {
				{return acc;}
			}
			case PLUS: {
				{return acc;}
			}
			default:
				throw new AssertionError();
		}
	}

}
