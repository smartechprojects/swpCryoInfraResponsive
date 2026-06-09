package com.eurest.supplier.service;

import com.eurest.supplier.dao.ReportDao;
import com.eurest.supplier.dto.ReportParameterValue;
import com.eurest.supplier.model.ReportDataSource;
import com.eurest.supplier.model.ReportParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service("reportParameterService")
public class ReportParameterService {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private DataSource dataSource;

    public List<ReportParameter> getAllParameters() {
        return reportDao.getAllReportParameters();
    }

    public ReportParameter getParameterById(Integer id) {
        return reportDao.getReportParameterById(id);
    }

    public ReportParameter saveParameter(ReportParameter parameter) {
        return reportDao.saveReportParameter(parameter);
    }

    public void deleteParameter(Integer id) {
        ReportParameter parameter = reportDao.getReportParameterById(id);
        if (parameter != null) {
            reportDao.deleteReportParameter(parameter);
        }
    }

    public List<ReportParameterValue> getParameterValues(Integer parameterId) throws Exception {
        List<ReportParameterValue> values = new ArrayList<ReportParameterValue>();
        ReportParameter param = reportDao.getReportParameterById(parameterId);
        if (param == null) {
            return values;
        }

        String type = param.getType();

        if ("List".equalsIgnoreCase(type)) {
            return parseListValues(param.getData());
        } else if ("Boolean".equalsIgnoreCase(type)) {
            String data = param.getData();
            if (data == null || !data.contains("|")) {
                data = "true:Sí|false:No";
            }
            return parseListValues(data);
        } else if ("Query".equalsIgnoreCase(type) || (param.getData() != null && param.getData().trim().toUpperCase().startsWith("SELECT"))) {
            return getParamValuesFromDataSource(param);
        }

        return values;
    }

    private List<ReportParameterValue> parseListValues(String data) {
        List<ReportParameterValue> list = new ArrayList<ReportParameterValue>();
        if (data == null || data.trim().isEmpty()) {
            return list;
        }
        StringTokenizer st = new StringTokenizer(data, "|");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String id = token;
            String description = token;

            StringTokenizer paramValue = new StringTokenizer(token, ":");
            if (paramValue.countTokens() == 2) {
                id = paramValue.nextToken();
                description = paramValue.nextToken();
            }
            list.add(new ReportParameterValue(id.trim(), description.trim()));
        }
        return list;
    }

    private List<ReportParameterValue> getParamValuesFromDataSource(ReportParameter param) throws Exception {
        List<ReportParameterValue> list = new ArrayList<ReportParameterValue>();
        if (param.getData() == null || param.getData().trim().isEmpty()) {
            return list;
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        boolean isExternalConn = false;

        try {
            if (param.getDataSource() != null) {
                ReportDataSource ds = param.getDataSource();
                if (ds.getJndi() != null && ds.getJndi()) {
                    javax.naming.InitialContext ctx = null;
                    try {
                        ctx = new javax.naming.InitialContext();
                        DataSource jndiDs = (DataSource) ctx.lookup(ds.getUrl());
                        conn = jndiDs.getConnection();
                    } finally {
                        if (ctx != null) {
                            ctx.close();
                        }
                    }
                } else {
                    Class.forName(ds.getDriverClassName());
                    conn = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
                }
                isExternalConn = true;
            } else {
                conn = dataSource.getConnection();
            }

            pStmt = conn.prepareStatement(param.getData());
            rs = pStmt.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            boolean multipleColumns = rsMetaData.getColumnCount() > 1;

            while (rs.next()) {
                String id = rs.getString(1);
                String description = multipleColumns ? rs.getString(2) : id;
                list.add(new ReportParameterValue(id != null ? id.trim() : "", description != null ? description.trim() : ""));
            }
        } finally {
            if (rs != null) rs.close();
            if (pStmt != null) pStmt.close();
            if (conn != null && isExternalConn) conn.close();
        }

        return list;
    }
}
