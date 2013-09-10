package trollAim;
import java.io.*;

public class PowerNode extends Node
{
    Node Base;
    Node Exponent;

    public PowerNode(Node b, Node e) {
	Base = b;
	Exponent = e;
    }

    public Node CopySubtree() { 
      PowerNode pn = new PowerNode(Base.CopySubtree(), Exponent.CopySubtree());
      return pn;
    }

    public Node ExpandSubtree() {
      return this;
    }

    public int CountVariables() {
      return Base.CountVariables();
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
      Base.PrintSubtree();
      System.out.print("^(");
      Exponent.PrintSubtree();
      System.out.print(")");
    }

    public void PrintTerm(PrintStream pout) {
    pout.print("(");
    Base.PrintTerm(pout);
    pout.print("^");
    Exponent.PrintTerm(pout);
    pout.print(")");
  }

    public int PowerErrorCheck() {
      return (Base.CountVariables() + Exponent.CountVariables());
    }

  public int ProductErrorCheck() {
    return (Base.CountVariables() + Exponent.CountVariables());
  }
    
} // class PowerNode

