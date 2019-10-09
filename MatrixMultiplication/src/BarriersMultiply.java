import java.security.SecureRandom;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//Multiply 2 threads using multithreading and barriers.

public class BarriersMultiply {
	
	public static void main(String[] args) throws InterruptedException {
		int[][] arrayA=new int[30][30];
		int[][] arrayB=new int[30][30];
		int[][] arrayC=new int [arrayA.length][arrayB[0].length];
		int[] rowNumber= {0};
		
		createArray(arrayA);
		createArray(arrayB);
		CyclicBarrier barrier=new CyclicBarrier(3);
		//pass reference of arrays, barrier and ThreadName
		BarrierThread A=new BarrierThread(arrayA,arrayB,arrayC,barrier,rowNumber,"ThreadA");
		BarrierThread B=new BarrierThread(arrayA,arrayB,arrayC,barrier,rowNumber,"ThreadB");
		BarrierThread C= new BarrierThread(arrayA,arrayB,arrayC,barrier,rowNumber,"ThreadC");
		
		A.start();
		B.start();
		C.start();
		
		A.join();
		B.join();
		C.join();
		
		printArray(arrayA);
		System.out.println();
		printArray(arrayB);
		System.out.println();
		printArray(arrayC);
	}
	
	public static void printArray(int[][] a) {
		for(int i=0;i<a.length;i++) {
			for(int j=0;j<a[0].length;j++) {
				System.out.print(a[i][j]+",");
			}
		System.out.println();
		}
	}
	public static void createArray(int[][] a) {
		SecureRandom randomNumbers=new SecureRandom();
		for( int i=0; i< a.length;i++) 
			for(int j=0;j<a[0].length;j++) 
				a[i][j]=randomNumbers.nextInt(10);
	}
}

class BarrierThread extends Thread {
	
	int[][] arrayA;
	int[][] arrayB;
	int[][] arrayC;
	CyclicBarrier barrier;
	int[] rowNumber;
	String threadName;
	
	public BarrierThread(int[][] arrayA, int[][] arrayB,int[][] arrayC,CyclicBarrier barrier,int[] rowNumber,String name) {
		this.arrayA=arrayA;
		this.arrayB=arrayB;
		this.arrayC=arrayC;
		this.barrier=barrier;
		this.rowNumber=rowNumber;
		threadName=name;
	}

	@Override
	public void run() {
		//loops until the last element of C is filled.
		int await=0;
		while(arrayC[arrayC.length-1][arrayC[0].length-1]==0) {
		synchronized(arrayA) {
			synchronized(arrayB) {
				int row=rowNumber[0];
				int count = 5 ;
				while(count>0 && row<arrayC.length) {
					for(int i=0;i<arrayB[0].length;i++) { //loops according to the number of columns of ArrayB
						int sum=0;
						for(int j=0;j<arrayA[0].length;j++) {
							int fromA=arrayA[row][j];
							int fromB=arrayB[j][i];
							int mul=fromA*fromB;
							sum+=mul;
						}
						arrayC[row][i]=sum;
					}
				row++;
				count--;
				}
				rowNumber[0]=row;
			}
		}
			try {
				System.out.println(threadName + " waiting on barrier.");
				await=barrier.await();
				if(await==0) {
					System.out.println("Barrier is broken.");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(threadName+ " ending");

	}
	
	
}
