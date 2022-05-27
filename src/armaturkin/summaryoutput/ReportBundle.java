package armaturkin.summaryoutput;

/**
 * @author Andrey Korneychuk on 26-May-22
 * @version 1.0
 */
public class ReportBundle {

	private TableHeaderResult tableHeaderResult;
	private ParsedRange parsedRange;

	public ReportBundle() {
		tableHeaderResult = new TableHeaderResult();
		parsedRange = new ParsedRange();
	}

	public TableHeaderResult getTableHeaderResult() {
		return tableHeaderResult;
	}

	public void setTableHeaderResult(TableHeaderResult tableHeaderResult) {
		this.tableHeaderResult = tableHeaderResult;
	}

	public ParsedRange getParsedRange() {
		return parsedRange;
	}

	public void setParsedRange(ParsedRange parsedRange) {
		this.parsedRange = parsedRange;
	}
}