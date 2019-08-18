package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

    SalesDao salesDao;
    SalesReportDao salesReportDao;
    EcmService ecmService;

    private final String SALES_ACTIVITY = "SalesActivity";

    public SalesApp() {
        salesDao = new SalesDao();
        salesReportDao = new SalesReportDao();
        ecmService = new EcmService();
    }

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {
        if (salesId == null) {
            return;
        }

        Sales sales = getSalesDao().getSalesBySalesId(salesId);

        if (isNotEffectiveSales(sales)) {
            return;
        }

        List<SalesReportData> reportDataList = getSalesReportDao().getReportData(sales);
        List<SalesReportData> filterReportDataList = filterReportDataList(isSupervisor, reportDataList);

        filterReportDataList = filterRedundantData(maxRow, filterReportDataList);

        List<String> headers = getHeadersByNatTrade(isNatTrade);

        SalesActivityReport report = this.generateReport(headers, filterReportDataList);

        getEcmService().uploadDocument(report.toXml());

    }

    protected List<SalesReportData> filterRedundantData(int maxRow, List<SalesReportData> filterReportDataList) {
        List<SalesReportData> data = new ArrayList<>();
        for (int i = 0; i < filterReportDataList.size() && i < maxRow; i++) {
            data.add(filterReportDataList.get(i));
        }
        return data;
    }

    protected List<String> getHeadersByNatTrade(boolean isNatTrade) {
        List<String> headers;
        headers = isNatTrade ? Arrays.asList("Sales ID", "Sales Name", "Activity", "Time")
                : Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
        return headers;
    }

    protected List<SalesReportData> filterReportDataList(boolean isSupervisor, List<SalesReportData> reportDataList) {
        List<SalesReportData> filteredReportDataList = new ArrayList<>();
        for (SalesReportData data : reportDataList) {
            if (SALES_ACTIVITY.equalsIgnoreCase(data.getType())) {
                if (!data.isConfidential() || isSupervisor) {
                    filteredReportDataList.add(data);
                }
            }
        }
        return filteredReportDataList;
    }

    protected boolean isNotEffectiveSales(Sales sales) {
        Date today = new Date();
        return today.after(sales.getEffectiveTo())
                || today.before(sales.getEffectiveFrom());
    }

    protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
        // TODO Auto-generated method stub
        return null;
    }


    public SalesDao getSalesDao() {
        return salesDao;
    }

    public SalesReportDao getSalesReportDao() {
        return salesReportDao;
    }

    public EcmService getEcmService() {
        return ecmService;
    }
}
