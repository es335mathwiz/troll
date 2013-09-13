package gov.frb.ma.msu;
import java.io.*;

public class ConstantNode extends Node
{
  double Value;
  
  public ConstantNode(double v) {
    Value = v;
  }
  
  public Node CopySubtree() { 
    ConstantNode cn = new ConstantNode(Value);
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
    int eqnoPlus1 = eqno + 1;
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
    if (Value < 0.0)
      System.out.print("(" + Value + ")");
      else
	System.out.print(Value);
  }

  public void PrintTerm(PrintStream pout) {
    pout.print(Value);
  }

  public int PowerErrorCheck() {
    return 0;
  }

  public int ProductErrorCheck() {
    return 0;
  }
    
} // class ConstantNode

