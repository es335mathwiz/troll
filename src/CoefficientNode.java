package trollAim;
import java.io.*;

public class CoefficientNode extends Node
{
  String Name;
  
  public CoefficientNode(String s) {
    Name = s;
  }
  
  public Node CopySubtree() { 
    CoefficientNode cn = new CoefficientNode(Name);
    return cn;
  }

  public Node ExpandSubtree() {
    return this;
  }
  
  public int CountVariables() {
    return 0;
  }

  public Node FindVariable() {
    return null;
  }

  public void PrintGMatrixEntries(Model m, int eqno, int side,
				  PrintStream pout) {
    int eqnoPlus1;
    eqnoPlus1 = eqno + 1;
    System.err.println("Error in equation " + eqnoPlus1 +
		       ": Additive constants not permitted.\n");
    System.exit(1);
  }

  public void PrintHMatrixEntries(Model m, int eqno, int side,
				  PrintStream pout) {
    int eqnoPlus1 = eqno + 1;
    System.err.println("Error in equation " + eqnoPlus1 +
		       ": Additive constants not permitted.\n");
    System.exit(1);
 }

  public void PrintSubtree() {
    System.out.print(Name);
  }

  public void PrintTerm(PrintStream pout) {
    pout.print(Name);
  }

  public int PowerErrorCheck() {
    return 0;
  }

  public int ProductErrorCheck() {
    return 0;
  }
  
} // class CoefficientNode

