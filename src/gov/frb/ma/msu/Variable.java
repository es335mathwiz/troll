package gov.frb.ma.msu;

public class Variable
{
    String Name;
    int Type;     // must be AMA.Endogenous or AMA.Exogenous.
    int DataType; // must be AMA.Data, AMA.Notd, AMA.Dtrm, or AMA.Innov.

    public Variable(String s) {
	Name = s;
    }

    public void setType(int n) { Type = n; }
    public void setDataType(int n) { DataType = n; }

}
