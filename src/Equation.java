package trollAim;
public class Equation
{
  String Name;
  Node LHS;
  Node RHS;
  int EqType;  // must be Aim.Stoch or Aim.Imposed.

  public Equation(String s, Node left, Node right, int etype) {
    Name = s;
    LHS = left;
    RHS = right;
    EqType = etype;
  }
  
  public Equation(String s, int etype) {
    Name = s;
    EqType = etype;
    // Supply default values for LHS and RHS. For certain model files with
    // semantic errors (e.g. an exog variable w/similarly-named equation
    // specified to have eqtype "Imposed"), this may keep parser from crashing.
    LHS = new CoefficientNode("FakeCoefficientName");
    RHS = new CoefficientNode("FakeCoefficientName");
  }

  public void setLHS(Node n) {
    LHS = n;
  }
  
  public void setRHS(Node n) {
    RHS = n;
  }

  public int getEqType() {
    return EqType;
  }
  
  public void Print() {
    LHS.PrintSubtree();
    System.out.print(" =\n         ");
    RHS.PrintSubtree();
    System.out.println("\n\n");
}

  public void ExpandSubtrees() {
    LHS = LHS.ExpandSubtree();
    RHS = RHS.ExpandSubtree();
  }

}

