package gov.frb.ma.msu;
import java.io.*;


public class Model
{
  String Name;  // name of model
    //   int NUEq;         // number of unsorted equations
  int NEq;         // number of equations
  int NLag;        // maximum lag in model
  int NLead;       // maximum lead in model

    //  Equation[] UnsortedEquations = new Equation[AMA.Max_Array_Size]; // array of 
    //                                // Equations based on partial information in
    //                                // eqtype statements
  Equation[] Equations = new Equation[AMA.Max_Array_Size];   // array of Equations
  String[] Coefficients = new String[AMA.Max_Array_Size];    // coefficient names
  Variable[] Variables = new Variable[AMA.Max_Array_Size];  // Variable objects
    //  String[] EndogenousVariables = new String[AMA.Max_Array_Size]; // endog var names

  int NVars = 0;       // number of variables
    //  int NEndog = 0;      // number of endogenous variables
  int NCoeffs = 0;     // number of coefficients

  public Model(String s) {
      Name = s;
  }

  public int ErrorCheck() { 
    int i;
    int errorFound = 0;

    System.err.println("Checking for errors......");

    errorFound += CheckEquations();
    errorFound += CheckVariables();

    for (i = 0; i < NEq; i++)
    {
	if ((Equations[i].LHS.PowerErrorCheck() > 0) ||
	    (Equations[i].RHS.PowerErrorCheck() > 0)) {
	    System.err.print("Error in equation #" + (i+1) + ": ");
	    System.err.print("Variables cannot be raised to a power nor ");
	    System.err.print("appear\n                    in an exponent");
	    System.err.println(" or denominator.");
	    errorFound++;
	}
	
	if ((Equations[i].LHS.ProductErrorCheck() > 1) ||
	    (Equations[i].RHS.ProductErrorCheck() > 1)) {
	    System.err.print("Error in equation #" + (i+1) + ": ");
	    System.err.println("Equation has additive constant or is nonlinear");
	    System.err.println("                    in its variables.");
	    errorFound++;
	}
    }
    return errorFound;
  }
    
    public int CheckEquations() {
	// check that each endogenous variable is the name of exactly one
	// equation, and do error-checking on eqtypes.
	int errors = 0;
	int[] EquationsNamedAfterVariable = new int[AMA.Max_Array_Size];
	int vindex;
	int eindex;
	for (vindex = 0; vindex < NVars; vindex++)
	    EquationsNamedAfterVariable[vindex] = 0;
	for (eindex = 0; eindex < NEq; eindex++) // count eqns with each var name
	    {
		vindex = FindVariableIndex(Equations[eindex].Name);
		if (vindex >= 0)
		    (EquationsNamedAfterVariable[vindex])++;
	    }
	for (vindex = 0; vindex < NVars; vindex++) // check the results
	    {
		// System.err.println("Variable " + Variables[vindex].Name +
		//	       " has Type " + Variables[vindex].Type +
		//	       " and DataType " + Variables[vindex].DataType);

		if ((Variables[vindex].Type == AMA.Endogenous) &&
		    (EquationsNamedAfterVariable[vindex] == 0)) {
		    System.err.println("Error: There is no equation for the "
				       + "endogenous variable " +
				       Variables[vindex].Name + ".");
		    errors++; 
		}
		else if ((Variables[vindex].Type == AMA.Endogenous) &&
			 (EquationsNamedAfterVariable[vindex] > 1)) {
		    System.err.println("Error: There is more than one equation "
				       + "for the endogenous variable " +
				       Variables[vindex].Name + ".");
		    errors++; 
		}
 		else if ((Variables[vindex].Type == AMA.Exogenous) &&
		    (EquationsNamedAfterVariable[vindex] > 0)) {
		    System.err.println("Error: There is an equation named after "
				       + "the exogenous variable " +
				       Variables[vindex].Name + ".");
		    
		    errors++; 
		}
	    }		
	   return errors;
    }
    
    public int CheckVariables() { 
	int i;
	int eqindex;
	int errors = 0;
	for (i = 0; i < NVars; i++)
	    {
		if (Variables[i].Name.equals("one")) {
		    System.err.println("Error: 'one' is a reserved variable name.");  
		    errors++;
		}
		switch (Variables[i].Type) {
		case AMA.Endogenous:
		    switch (Variables[i].DataType) {
		    case AMA.Dtrm:
		    case AMA.Innov:
			System.err.println("Error in declaration of variable " +
					   Variables[i].Name +
				       ". Endogenous variables must have datatype " +
					   "'data' or 'notd'.");
			errors++;
			break;
		case AMA.Data:
		    case AMA.Notd:
			break;
		    }
		    break;
		case AMA.Exogenous:
		    switch (Variables[i].DataType) {
		    case AMA.Data:
		    case AMA.Notd:
			System.err.println("Error in declaration of variable " + 
					   Variables[i].Name +
					   ". Exogenous variables must have datatype " +
					   "'dtrm' or 'innov'.");
			errors++;
			break;
		    case AMA.Dtrm:
			eqindex = FindEquationIndex(Variables[i].Name);
			if (eqindex >= 0) { // the eqn has been specified
			    if (Equations[eqindex].getEqType() !=
				AMA.Imposed) {
				System.err.println("Error in declaration of variable "
						   + Variables[i].Name +
						   ". Exogenous variables with datatype "
						   + "'dtrm' must have eqtype 'imposed'.");
				errors++;
			    }
			}
			break;
		    case AMA.Innov:
			eqindex = FindEquationIndex(Variables[i].Name);
			if (eqindex >= 0) { // the eqn has been specified
			    if ((Equations[eqindex]).getEqType() !=
				AMA.Stoch) {
				System.err.println("Error in declaration of variable "
						   + Variables[i].Name +
						   ". Exogenous variables with datatype "
						   + "'innov' must have eqtype 'stoch'.");
				errors++;
			    }
			}
			break;
		    }
		    break;
		}
	    }
	return errors;
    }
    
    
    public void AddEquation(Equation e) {
	Equations[NEq] = e;
	NEq++;
    }
    
    //    public void AddUnsortedEquation(Equation e) {
    //	UnsortedEquations[NUEq] = e;
    //	NUEq++;
    //    }
    
    public void AddCoefficient(String s) {
	Coefficients[NCoeffs] = s;
	NCoeffs++;
    }

  public void AddVariable(Variable v) {
      Variables[NVars] = v;
      NVars++;
      //      if (v.Type == AMA.Endogenous) {
      //	EndogenousVariables[NEndog] = v.Name;
      //	NEndog++;
      //      }
  }

  public int FindCoefficientIndex(String s) {
    // returns the index of the String in Coefficients
    // that matches the String s, or -1 if there is no match.
    int i = 0;
    while ((i < NCoeffs) && !(Coefficients[i].equals(s)))
      i++;
    if (i < NCoeffs)
      return i;
    else
      return -1;
  }

  public int FindEquationIndex(String s) {
    // returns the index of the equation in Equations
    // that matches the String s, or -1 if there is no match.
    int i = 0;
    while ((i < NEq) && !(Equations[i].Name.equals(s)))
      i++;
    if (i < NEq)
      return i;
    else
      return -1;
  }

    //  public int FindUnsortedEquationIndex(String s) {
    //    // returns the index of the equation in UnsortedEquations
    //    // that matches the String s, or -1 if there is no match.
    //    int i = 0;
    //    while ((i < NUEq) && !(UnsortedEquations[i].Name.equals(s)))
    //      i++;
    //    if (i < NUEq)
    //      return i;
    //    else
    //      return -1;
    //  }

  public int FindVariableIndex(String s) {
    // returns the index of the variable in Variables whose
    // name matches the String s, or -1 if there is no match.
    int i = 0;
    while ((i < NVars) && !((Variables[i].Name).equals(s)))
      i++;
    if (i < NVars)
      return i;
    else
      return -1;
  }

  public void Print() { // for debugging
    int i;
    for (i = 0; i < NEq; i++) {
      System.out.println("Equation #" + (i+1) + ":");
      Equations[i].Print();
    }
  }

  public void ExpandSubtrees() {
    int i;
    for (i = 0; i < NEq; i++)
      Equations[i].ExpandSubtrees();
  }

  public void setName(String s) { Name = s; }
  public void setNEq(int n) { NEq = n; }
  public void setNLag(int n) { NLag = n; }
  public void setNLead(int n) { NLead = n; }
  public void setNCoeffs(int n) { NCoeffs = n; }

  public void BuildExogenousEquations() {
      int i;
      int index;
      VariableNode vn1;
      VariableNode vn2;
      ConstantNode cn;
      ProductNode pn;
      Variable v;
      Equation e;
      Variable onev;

      // create equations for all exogenous variables for which there
      // is no user-supplied equation with the same name. handle the
      // variable named "one" differently.

      onev = new Variable((String) "one");
      onev.setType(AMA.Exogenous);
      onev.setDataType(AMA.Dtrm);
      AddVariable(onev);

      for (i = 0; i < NVars; i++) {
	  if (Variables[i].Type == AMA.Exogenous) {
	      if (Variables[i].DataType == AMA.Innov) {
		  index = FindEquationIndex(Variables[i].Name);
		  if (index < 0) {
		      vn1 = new VariableNode(Variables[i].Name, 0, 0);
		      vn2 = new VariableNode((String) "one", 0, 0);
		      cn = new ConstantNode(0.0);
		      pn = new ProductNode(cn, vn2);
		      e = new Equation(Variables[i].Name, vn1, pn, AMA.Stoch);
		      AddEquation(e);
		  } // end of "if (index < 0)" block
	      } // end of "if (Variables[i].DataType == AMA.Innov)" block
	      else if (Variables[i].DataType == AMA.Dtrm) {
		  index = FindEquationIndex(Variables[i].Name);
		  if (index < 0) {
		      vn1 = new VariableNode(Variables[i].Name, 0, 0);
		      vn2 = new VariableNode(Variables[i].Name, -1, 0);
		      e = new Equation(Variables[i].Name, vn1, vn2, AMA.Imposed);
		      AddEquation(e);
		  } // end of "if (index < 0)" block
	      } // end of "else if (Variables[i].DataType == AMA.Dtrm)" block
	  } // end of "if (Variables[i].Type == AMA.Exogenous)" block
      } // end of for loop
  }

    public void PrintTerseOutput() {
	System.out.println("Model: " + Name + " (" + NVars + " variables)");
    }

    public void PrintVerboseOutput() {
	int i;
	int j;
	int eqindex;
	System.out.println("Model: " + Name + " (" + NVars + " variables)");
	for (i = 0; i < NVars; i++) {
	    System.out.print("#" + (i+1) + ":");
	    for (j = 0; j < (3 - ((i+1)/10)); j++)
		System.out.print(" ");
	    System.out.print(Variables[i].Name);
	    for (j = 0; j < (20 - Variables[i].Name.length()); j++)
		System.out.print(" ");
	    switch (Variables[i].Type) {
	    case AMA.Endogenous: System.out.print("Endogenous  "); break;
	    case AMA.Exogenous: System.out.print("Exogenous   "); break;
	    }
	    switch (Variables[i].DataType) {
	    case AMA.Data: System.out.print("Data   "); break;
	    case AMA.Notd: System.out.print("Notd   "); break;
	    case AMA.Dtrm: System.out.print("Dtrm   "); break;
	    case AMA.Innov: System.out.print("Innov  "); break;
	    }
	    switch (Equations[FindEquationIndex(Variables[i].Name)].getEqType()) {
	    case AMA.Stoch: System.out.print("Stochastic"); break;
	    case AMA.Imposed: System.out.print("Imposed   "); break;
	    }
	    System.out.println("");
	}
    }

  public void PrintFunctions() {
    int i;
    PrintStream dataPS;
    PrintStream matrixPS;
    String lcName = Name;
    lcName.toLowerCase();
    String dataFileName = lcName + "_AMA_data.m";
    String matrixFileName = lcName + "_AMA_matrices.m";
    
    try {
	dataPS = new PrintStream(new FileOutputStream(dataFileName));

	dataPS.println("function [param_,np,modname,neq,nlag,nlead,eqname_,eqtype_,endog_,delay,vtype_] = ...");
	dataPS.println("     " + lcName + "_AMA_data()\n");
	dataPS.println();
	dataPS.println("% " + lcName + "_AMA_data()");
	dataPS.println("%     This function will return various information about the AMA model,");
	dataPS.println("%     but will not compute the G and H matrices.");
	dataPS.println("     delay=[];%dummy for modelez compatability");
	dataPS.println();
	dataPS.println("  eqname = cell(" + NEq + ", 1);");
	dataPS.println("  param = cell(" + NCoeffs + ", 1);");
	dataPS.println("  endog = cell(" + NEq + ", 1);");
	dataPS.println("  vtype = zeros(" + NEq + ", 1);");
	dataPS.println("  eqtype = zeros(" + NEq + ", 1);");
	dataPS.println();
	dataPS.println("  modname = '" + Name + "';");
	dataPS.println("  neq = " + NEq + ";");
	dataPS.println("  np = " + NCoeffs + ";");
	dataPS.println("  nlag = " + NLag + ";");
	dataPS.println("  nlead = " + NLead + ";");
	dataPS.println();

	for (i = 0; i < NEq; i++)
	    dataPS.println("  eqname(" + (i+1) + ") = cellstr('" +
			   Equations[i].Name + "');");
	dataPS.println("  eqname_ = char(eqname);");
	dataPS.println();

	for (i = 0; i < NEq; i++) {
	    dataPS.print("  eqtype(" + (i+1) + ") = " + Equations[i].EqType +
			   ";   ");
	    if (i % 3 == 2)
	      dataPS.println();
	}
	if (i % 3 != 1)
	  dataPS.println();
	dataPS.println("  eqtype_ = eqtype;");
	dataPS.println();

	for (i = 0; i < NCoeffs; i++)
	dataPS.println("  param(" + (i+1) + ") = cellstr('" + Coefficients[i]
		       + "');");
	dataPS.println("  param_ = char(param);");
	dataPS.println();

	for (i = 0; i < NVars; i++)
	  dataPS.println("  endog(" + (i+1) + ") = cellstr('" +
			 Variables[i].Name + "');");
	dataPS.println("  endog_ = char(endog);");
	dataPS.println();

	// No delay values are generated. If they were, they would probably
	// be printed here.

	for (i = 0; i < NEq; i++) {
	  dataPS.print("  vtype(" + (i+1) + ") = " + Variables[i].DataType +
		       ";   ");
	  if (i % 3 == 2)
	    dataPS.println();
	}
	if (i % 3 != 1)
	  dataPS.println();
	dataPS.println("  vtype_ = vtype;");
	dataPS.print("\n\n\n");

	dataPS.close();
    } catch (Exception e) {
	System.err.println("ERROR: " + e.getMessage());
    }

/*****************************************************************
  Now print out the function compute_AMA_matrices(). This function
  will compute the G and H matrices.  Actually this is a script not
  a function.  It is easier to deal with the parameters in Matlab if
  this part is a script since you don't have to declare them globals
  or reassign them inside the function.
******************************************************************/

    try {
      matrixPS = new PrintStream(new FileOutputStream(matrixFileName));
            
      matrixPS.println("% " + lcName + "_AMA_matrices()");
      matrixPS.println("%     This script will compute the G and H matrices.");
      matrixPS.println();

      matrixPS.println("  g = zeros(" + NEq + ", " + ((NLag+1)*NEq)
		       + ");");
      matrixPS.println("  h = zeros(" + NEq + ", " + ((NLag+1+NLead)*NEq)
		       + ");");
      matrixPS.println();

      for (i = 0; i < NEq; i++) {
	Equations[i].LHS.PrintGMatrixEntries(this, i, AMA.Left_Side,
					     matrixPS);
	Equations[i].RHS.PrintGMatrixEntries(this, i, AMA.Right_Side,
					     matrixPS);
	Equations[i].LHS.PrintHMatrixEntries(this, i, AMA.Left_Side,
					     matrixPS);
	Equations[i].RHS.PrintHMatrixEntries(this, i, AMA.Right_Side,
					     matrixPS);
      }

      matrixPS.println();
      matrixPS.println("  cofg = g;");
      matrixPS.println("  cofh = h;");

      matrixPS.close();
    } catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }
  
}

