### This repo demonstrates thread interefence and working with the Java.util.Concurrent package

1. After initial commit with error output, to rectify the code we synchronize on the ArrayList
since both threads share it.
2. The output for this commit after synchronizng the buffer in the MyProducer and MyConsumer threads
displays "Adding...var num" and "Removing...var num" on the next line till the loop completes and 
eventually prints out "Adding EOF and Exiting", the "Exiting" twice