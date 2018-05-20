package similarity;

import org.jblas.DoubleMatrix;                  // For fast matrix computation 
import org.apache.commons.math3.complex.*;         // For complex numbers needed in Checbyshev polynomials
import java.lang.Math;

public class IEApprox {
    
    
    private int k;
    private int n;
    
    
    // The matrix containing the binomial coefficients
    private DoubleMatrix matrixM;
    private double [][]M;
    
    // The vector containing the Q_n 
    private double[] Q;
    private DoubleMatrix matrixQ;
    
    // The result matrix
    DoubleMatrix coefficients;
    
    public IEApprox(int k_,int n_)
    {
        
        k = k_;
        n = n_;
        M = new double[k][k];
        Q = new double[k];
        
        long value;

        // Fill the binomial matrix using the Linial,Nisan method
        for(int i = 0; i < k; i++)
            for(int j = i; j < k;j++)
            {
                value = combinations(j+1,i+1);
                if((i+j) % 2 == 0)
                 M[i][j] = value;   
                else
                 M[i][j] = -1*value;   
            }
        matrixM = new DoubleMatrix(M); 
       // System.out.println("MATRIX M "+matrixM);
      
        // Fill the Q vector
        for(int i = 0; i < k;i++)
            Q[i] = qFunction(k,  n, i+1);
     // System.out.println("----------------------");
     // System.out.println("MATRIX Q ");
    //  for(int j = 0; j < k;j++)
    //      System.out.print(Q[j]+"   ");
    //  System.out.println("\n-----------------\n");
        //matrixQ = new DoubleMatrix(Q);
        matrixQ = new DoubleMatrix(1,k,Q);
        //System.out.println("MATRIX Q"+matrixQ);
        
        
        // Do the matrix multiplication Q*M
        coefficients = matrixQ.mmul(matrixM);
        //System.out.println("COEFFICIENTS "+ coefficients);
    }
    
    public DoubleMatrix getCoefficients()
    {
        return coefficients;
    }
    
    // Binomial Coefficient (naive computation)
    
    private static long combinations(int n, int k) {
		long coeff = 1;
		for (int i = n - k + 1; i <= n; i++) {
			coeff *= i;
		}
		for (int i = 1; i <= k; i++) {
			coeff /= i;
		}
		return coeff;
	}
    
    
    private static double chebyshevPolynomial(double k,double x)
    {        
        double value = Math.pow(x,2) - 1;
        //System.out.println(value);
        
        Complex complexV = new Complex(value,0);
        Complex complexX = new Complex(x,0);
        
        Complex sqr1 = complexX.add(complexV.sqrt());
        Complex sqr2 = complexX.subtract(complexV.sqrt());        
        
        //System.out.println(sqr1);
        //System.out.println(sqr2);
        
        sqr1 = sqr1.pow(k);
        sqr2 = sqr2.pow(k);        

        //System.out.println(sqr1);
        //System.out.println(sqr2);

        Complex result = sqr1.add(sqr2);
        result = result.divide(2);

        return result.getReal();
    }
    
    
    

    
    
    
    
    
    private double qFunction(int k, int n, int x)
    {
       // q[k_, n_, x_] := 
       // 1 - (ChebyshevT[k, (2 x - (n + 1))/(n - 1)]/
       //      ChebyshevT[k, (1 - n)/(n - 1)])
        
     //   System.out.println("K = "+k + " , N = "+ n+", X = "+ x+" ----> ");
        
        double arg1 = (double)(2*x - (n+1))/(double)(n-1);
        double arg2 = (double)(-n-1)/(double)(n-1);
        double t1=chebyshevPolynomial(k,arg1);
        double t2=chebyshevPolynomial(k,arg2);
        
    /*    System.out.println("1-(t1)/t(2)");
        System.out.println("t1"+t1);
        System.out.println("t2"+t2);
*/

        double rez = (double)1.0 - ((double)t1/(double)t2);
        return rez;
        
    }
    
    
    
    public static void main(String[] args) {
       
      
        IEApprox ie = new IEApprox(4,20);
      DoubleMatrix coefficients = ie.getCoefficients();
        //System.out.println(ie); 
        for(int i=0;i<4;i++) 
            System.out.println(coefficients.get(0,i)+"");
    
         
    }
}