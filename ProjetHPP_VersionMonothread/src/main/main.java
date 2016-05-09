package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;

public class main {

	public static void main(String[] args) throws IOException{
		Post post= new Post(new Timestamp(0), 0,0, "lol", "Arnaud");
		Comment comment =  new Comment(new Timestamp(0), new BigInteger("0"),new BigInteger("0"), "lol", "Arnaud",new BigInteger("12"),new BigInteger("0"));
		post.loadfile();
		try {


			FileReader file = new FileReader("comments.txt");
			BufferedReader bufferedReader = new BufferedReader(file);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while((line = bufferedReader.readLine()) != null){
				line = bufferedReader.readLine();


				//Comment c =  new Comment(new Timestamp(0), new BigInteger("0"),new BigInteger("0"), "lol", "Arnaud",new BigInteger("12"),new BigInteger("0"));
				//c.loadFromString(line);
				//System.out.println(c);
			}///
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}







}
