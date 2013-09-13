package gov.frb.ma.msu;
import java.io.*;

public class SumNode extends Node
{
  Node Summand1;
  Node Summand2;

  public SumNode(Node n1, Node n2) {
    Summand1 = n1;
    Summand2 = n2;
  }
  
  public Node CopySubtree() { 
    SumNode sn = new SumNode(Summand1.CopySubtree(), Summand2.CopySubtree());
    return sn; 
  }

  public Node ExpandSubtree() {
    Summand1 = Summand1.ExpandSubtree();
    Summand2 = Summand2.ExpandSubtree();
    return this;
  }

  public int CountVariables() {
    return (Summand1.CountVariables() + Summand2.CountVariables());
  }

  public Node FindVariable() {
    Node n = Summand1.FindVariable();
    if (n != null)
      return n;
    else
      return Summand2.FindVariable();
  }

  public void PrintGMatrixEntries(Model m, int eqno, int side,
				  PrintStream pout) {
    Summand1.PrintGMatrixEntries(m, eqno, side, pout);
    Summand2.PrintGMatrixEntries(m, eqno, side, pout);
  }

  public void PrintHMatrixEntries(Model m, int eqno, int side,
				  PrintStream pout) {
    Summand1.PrintHMatrixEntries(m, eqno, side, pout);
    Summand2.PrintHMatrixEntries(m, eqno, side, pout);
  }

  public void PrintSubtree() {
    System.out.print("(");
    Summand1.PrintSubtree();
    System.out.print(" +\n            ");
    Summand2.PrintSubtree();
    System.out.print(")");
  }

  public void PrintTerm(PrintStream pout) {
    pout.print("(");
    Summand1.PrintTerm(pout);
    pout.print("+");
    Summand2.PrintTerm(pout);
    pout.print(")");
  }

  public int PowerErrorCheck() {
    return Summand1.PowerErrorCheck() + Summand2.PowerErrorCheck();
  }

  public int ProductErrorCheck() {
    // If either subtree has a value of zero, then there is an
    // additive constant term. In this case return 2 to assure 
    // that the error is detected. Otherwise, return larger value 
    // of subtree values.
    int Result1;
    int Result2;
    Result1 = Summand1.ProductErrorCheck();
    Result2 = Summand2.ProductErrorCheck();
    if ((Result1 == 0) || (Result2 == 0))
      return 2;
    else if (Result1 > Result2)
      return Result1;
    else
      return Result2;
  }
  
} // class SumNode

