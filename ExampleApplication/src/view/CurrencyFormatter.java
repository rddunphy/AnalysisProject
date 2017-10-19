package view;

public class CurrencyFormatter {
	
	public static final String DOLLAR = "\u0024";
	
	public static String format(long amount) {
		return DOLLAR + String.format("%.2f", (double) amount / 100);
	}

}
