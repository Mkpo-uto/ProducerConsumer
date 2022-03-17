package home;

import java.util.ArrayList;
/*
 * The java.util.concurrent package helps developers code threads
 * The classes in this package are there to help developers properly
 * synchronize code and makes it easier to work with multiple threads.
 * In this application, a thread adds data to a buffer, the Producer as 
 * well as two threads that can read data from the buffer the consumers.
 * The MyConsumer class calls the ArrayList isEmpty() method, then it calls 
 * remove() or get(), now that can be suspended after it calls isEmpty() giving
 * the producer or consumer the opportunity to change the contents of the ArrayList.
 * Given that scenario one potential problem could be that 
 * 1. The producer adds a string to the buffer, consumer1 might call isEmpty() method and that returns false or 
 * consumer1 is suspended or consumer2 calls isEmpty() and it returns false or consumer2 removes the string and consumer2 is suspended,
 * consumer1 runs again and calls get(0) and there is no data so we get an index out of bounds exception
 * 2. Another issue is that both consumers try to remove the same string N/B: The point is to remember that
 * ArrayList isn't thread safe so it is certainly possible for one thread to be suspended while in
 * the middle of running the remove() method before the item has actually been removed from the ArrayList - run code and results depends
 * on timing - The only thing we know for sure is we need to prevent thread interference
 * 
 */
import java.util.List;
import java.util.Random;

public class Mian {
	public static final String EOF = "EOF";

	public static void main(String[] args) {
		//create shared buffer, instance of MyProducer class and two instances of MyConsumer class
		//we pass the same buffer to all three instances
		//we create a thread for the producer and start it and we create two consumer threads
		List<String> buffer = new ArrayList<String>();
		MyProducer producer = new MyProducer(buffer, ThreadColor.ANSI_CYAN);
		MyConsumer consumer1 = new MyConsumer(buffer, ThreadColor.ANSI_PURPLE);
		MyConsumer consumer2 = new MyConsumer(buffer, ThreadColor.ANSI_BLUE);
		//with out knowledge of concurrency we know this code is going to have problems as three threads
		//are accessing the same ArrayList N/B: Remember ArrayList isn't synchronized
		new Thread(producer).start();
		new Thread(consumer1).start();
		new Thread(consumer2).start();
		
		/*
		 * For the commit associated with this comment thread interference no longer occurs
		 * when main() method is run because the MyProducer thread sleeps after writing a string 
		 * usually the MyConsumer thread will have a chance at that point to remove the last string
		 * added before the MyProducer thread itself runs again N/B: When removing sometimes only one of
		 * the MyConsumer threads gets the chance to remove the string unlike the "Exiting" display that is 
		 * printed out by both threads. Before Java 1.5 synchronization was the only arrow in our quiver for
		 * dealing with thread interference. One of its drawbacks is that thread that are blocked waiting to 
		 * execute synchronization code can't be interrupted. Once they are blocked they're stuck there until they get the lock for the object
		 * the code is synchronizing on and that can lead to problems. The second drawback is that the synchronized block must be
		 * within the same method i.e. We cannot start a synchronized block in one method and end the synchronized block in another
		 * method. The third drawback is that we cannot test to see if an object's intrinsic lock is available or find out any other
		 * information about that lock. Also if the lock isn't available we can't time out after we've waited for the lock for a while.
		 * When we reach the beginning of a synchronized block we can either get the lock and continue executing or block on that line of code until
		 * we get the lock and the fourth drawback is that if multiple threads re waiting to get a lock it is not first-come-first-serve as there isn't 
		 * a set order that JVM will choose the next thread that gets the lock. So the first thread that blocked could be the last thread to get the lock
		 * and vice versa. Instead of using synchronization we can use classes that implement the Java.util.Concurrent locks interface   
		 */

	}

}

class MyProducer implements Runnable{
	private List<String> buffer;//the buffer
	private String color;//represents the color
	
	//constructor accepts buffer that is going to be shared by consumers and the color to be 
	//used when it prints to the console
	public MyProducer(List<String> buffer, String color) {
		this.buffer = buffer;
		this.color = color;
	}
	
	//Producer writes the numbers 1 to 5 to the buffer as strings
	//before writing to the buffer it also prints the message "Adding" to the console
	//with the right color so you see what it is doing
	//After writing the number it goes to sleep for one second using the random class to generate 
	//that interval. When it finished writing the numbers it writes an EOF which is an end of file
	//string to the buffer to let the consumers know that there is no more data coming or to be processed
	public void run() {
		Random random = new Random();//Random object 
		String[] nums = { "1","2","3","4","5"};//String array of numbers
		
		for (String num : nums) {//for each to loop through String array nums
			try {
				System.out.println(color + "Adding..." + num);//print out color and number from String array
				synchronized (buffer) {
					buffer.add(num);//add to buffer
				}
				
				Thread.sleep(random.nextInt(1000));//sleep for one second
			} catch (InterruptedException e) {
				System.out.println("Producer was interrupted");//output message that it was interrupted
			}
		}
		
		System.out.println(color + "Adding EOF and exiting...");//The code does not need to include this display statement in
																//the synchronized block because if the MyProducer thread is suspended
																//between printing the line and actually adding the data it wont matter
																//A consumer could read any existing data from the buffer and in that case
																//it wont change what the MyProducer adds when it runs again
		synchronized (buffer) {
			buffer.add("EOF"); //At this point code is modified to synchronize the two add() method
							   //calls to buffer because ArrayList isn't thread safe. If code doesn't
							   //synchronize and the MyProducer is suspended in the middle of running 
							   //the add() method and one of the consumers then calls get() or remove()
							   //the internal integrity of the ArrayList might be compromised depending on timing
		}
	}
}


//MyConsumer class implements Runnable so it can run its code on the thread
//it accepts the buffer shared from the producer and the color it is going to
//use when it prints to the console. In the run() method the Consumer will loop
//until it reads the EOF file string from the buffer, inside the loop we check to
//see if there is anything to read, if there isn't we loop back around and check again
//and then keep checking until the buffer isn't empty. When that's the case it looks at 
//the first element in the buffer to see if it is that EOF, if it is it prints that it is
//exiting to the console and then breaks out of the loop. It doesn't remove the EOF string 
//because if it did that other Consumer threads will loop indefinitely. If EOF is removed 
//the condition that checks for EOF will never be true for the other Consumer threads and we
//wouldn't want that so that is why we are specifically exiting without removing that last entry
//Consumer threads have to leave EOF in the buffer which is fine because the Producer has indicated 
//there wont be any more data in the buffer at that point anyways. If the first Element isn't EOF
//it reads from the buffer and prints to the console and then goes back to the beginning of the loop
//In a real world application we wouldn't loop like that, we'll have the consumer sleep for a while
//before checking the buffer again
class MyConsumer implements Runnable{
	private List<String> buffer;//List of string objects buffer
	private String color;//string color
	
	public MyConsumer(List<String> buffer, String color) {
		this.buffer = buffer;
		this.color = color;
	}
	
	//this run() method is different from the one in the buffer because
	//we are going to be processing the buffer instead of creating it
	public void run() {
		//The goal of synchronizing the buffer and the whole if and if-else statements in
		//the while loop is we don't want the MyProducer thread or the other Consumer to modify the array list 
		//once the MyConsumer thread has checked whether the buffer is empty
		//we want all calls to methods in the array list to take place as a unit collectively and
		//they all run as a critical section
		while (true) {
			synchronized (buffer) {
			
				if(buffer.isEmpty()) {
					continue;	
				}
				if (buffer.get(0).equals(Mian.EOF)) {
					System.out.println(color + "Exiting");
					break;
				}else {
					System.out.println(color + "Removed " + buffer.remove(0));
				}
			}
		}
	}
}
