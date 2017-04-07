/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

   public static void main (String args[]) {
    
   }

   public void saveSession(String path, Main internalFrame){
	   
	   //Object [] o = new Object[accounts.size()];// = new Object();
	   int counter = 0;
//	   for (Account a: accounts){
//		   o[counter] = a;
//		   counter++;
//	   }
	   
	   try{
		   
		FileOutputStream fout = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(internalFrame);
		oos.close();
		System.out.println("Done");
		   
	   }catch(Exception ex){
		   ex.printStackTrace();
	   }
   }
   public Main loadSession(String path) throws IOException, ClassNotFoundException{
	// read object from file
				FileInputStream fis = new FileInputStream(path);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Main result = (Main) ois.readObject();
				ois.close();
				
//				//List <Account> a = new ArrayList <Account>();
//				for (int i = 0; i<result.length; i++){
//					//a.add((Account) result[i]);
//					return (Main) result[i];
//				}
				return result;
				//return a;
   }
}
