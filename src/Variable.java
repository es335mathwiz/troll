package trollAim;

public class Variable
{
    String Name;
    int Type;     // must be Aim.Endogenous or Aim.Exogenous.
    int DataType; // must be Aim.Data, Aim.Notd, Aim.Dtrm, or Aim.Innov.

    public Variable(String s) {
	Name = s;
    }

    public void setType(int n) { Type = n; }
    public void setDataType(int n) { DataType = n; }

}
