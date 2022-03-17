### This repo demonstrates thread interefence and working with the Java.util.Concurrent package

1. After initial commit with error output, to rectify the code we synchronize on the ArrayList
since both threads share it.
2. The output for this commit after synchronizng the buffer in the MyProducer and MyConsumer threads
displays "Adding...var num" and "Removing...var num" on the next line till the loop completes and 
eventually prints out "Adding EOF and Exiting", the "Exiting" twice
3. The purpose of this modification is to utilize the commit message on the local repo to assign it 
with the current issue #1 on upstream repo so issue #1 can be closed N/B: remember to close issue include
*close issue number i.e. close #1* in the associated commit message on local after the description. N/B: the previous
commit contains code that resolves the thread interfence that the enhanced issue raised. After this commit is pushed
upstream, the previous commit will be associated to the enhanced issue #1 by mention in it's comment section 
N/B: Git allows you to reference an issue by it's number in a commit without necessarily closing the commit 