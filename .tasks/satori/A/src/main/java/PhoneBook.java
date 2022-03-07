/**
 * PO 2021/22, Problem A - PhoneBook
 * @author YOUR NAME
 */

public class PhoneBook {
    public enum NumberFormat{ DIGITS, HYPHENED }

    public PhoneBook() {}
    public PhoneBook(NumberFormat format) {}
    public PhoneBook(int capacity) {}
    public PhoneBook(NumberFormat format, int capacity) {}

    public PhoneBook copyBook(){
      return null;
    }

    public int size(){
      return 0;
    }
    public int capacity(){
      return 0;
    }
    public boolean isEmpty(){
      return false;
    } 
    public boolean isFull(){
      return false;
    }

    public PhoneBook add(String number){
      return null;
    }
    public PhoneBook add(PhoneBook subBook){
      return null;
    }
    public void changeFormat(NumberFormat format){
    }

    public boolean contains(String number){
      return false;
    }

    public boolean contains(PhoneBook pb){
      return false;
    }
    public boolean elementOf(PhoneBook pb){
      return false;
    }
    public boolean subsetOf(PhoneBook pb){
      return false;
    }
    public boolean supersetOf(PhoneBook pb){
      return false;
    }
    public boolean equals(Object o){
      return false;
    }

    public String toString(){
      return null;
    }
}
