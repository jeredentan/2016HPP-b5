package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;

import javax.xml.stream.events.Comment;

import main.Commentaires;

public class mainComment {
	public static void main(String[] args) {
		
		try {
			FileReader file = new FileReader("comments.txt");
			BufferedReader bufferedReader = new BufferedReader(file);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while((line = bufferedReader.readLine()) != null){
				//line = bufferedReader.readLine();
				//System.out.println(line);
				Commentaires c =  new Commentaires(new Timestamp(0), 0,0, "lol", "Arnaud",12,0);
				c.loadFromString(line);
				System.out.println(c);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
