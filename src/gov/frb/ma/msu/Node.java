package gov.frb.ma.msu;
import java.io.*;

abstract class Node
{
  // CopySubtree() returns a pointer to a copy of the subtree rooted at 
  // this Node.
  public abstract Node CopySubtree();
  // ExpandSubtree() expands the subtree pointed to by this Node into 
  // sum of products form, and returns the resulting subtree. Note that
  // the base and exponent subtrees of a PowerNode will not be expanded
  // into sum of products form, which is fine since they can't contain
  // variables anyway, so they can't cause any nonlinearity.
  public abstract Node ExpandSubtree();
  // CountVariables() returns the number of occurrences of VariableNodes
  // it finds in the subtree rooted at this Node.
  public abstract int CountVariables();
  // FindVariable() returns the first VariableNode it finds in the subtree
  //  rooted at this Node.
  public abstract Node FindVariable();
  public abstract void PrintGMatrixEntries(Model m, int eqno,
					   int side, PrintStream pout);
  public abstract void PrintHMatrixEntries(Model m, int eqno,
					   int side, PrintStream pout);
  public abstract void PrintSubtree();
  public abstract void PrintTerm(PrintStream pout);
  // PowerErrorCheck() returns the number of instances of a variable
  // occurring inside of the base or exponent of a PowerNode in the 
  // subtree rooted at this Node.
  public abstract int PowerErrorCheck();
  // ProductErrorCheck() checks if either of the two following types
  // of errors are present: 1) two variables are multiplied together,
  // or 2) there is an additive constant term. ProductErrorCheck()
  // returns >1 if any such errors are detected, and 1 otherwise.
  // IMPORTANT: Note that the value 1, not 0, is returned if no errors
  // are found. It's not standard, but it makes it easier to write the
  // function.
  // 
  // To check for the first error condition, the number of variables
  // in the subtree rooted at each ProductNode is found; if it is
  // greater than 1, an error is propogated up the tree. To check
  // for the second error condition, the function looks for a subtree 
  // of a SumNode that contains no variables.
  //  
  // IMPORTANT: Note that this function relies on ExpandSubtree() 
  // having been called for this Node, since it assumes that there is
  // no ProductNode above any SumNode component in the subtree.
  public abstract int ProductErrorCheck();
  
} // class Node

