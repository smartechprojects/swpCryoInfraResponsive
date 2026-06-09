package com.eurest.supplier.service;

import com.eurest.supplier.dao.ReportDao;
import com.eurest.supplier.dao.UsersDao;
import com.eurest.supplier.model.Report;
import com.eurest.supplier.model.ReportGroup;
import com.eurest.supplier.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("reportService")
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private UsersDao usersDao;

    public List<Report> getAllReports() {
        return reportDao.getAllReports();
    }

    public Report getReportById(Integer id) {
        return reportDao.getReportById(id);
    }

    public Report getReportByName(String name) {
        return reportDao.getReportByName(name);
    }

    public Report saveReport(Report report) {
        return reportDao.saveReport(report);
    }

    public void deleteReport(Integer id) {
        Report report = reportDao.getReportById(id);
        if (report != null) {
            reportDao.deleteReport(report);
        }
    }

    public List<Report> getReportsForUser(String username) {
        Users user = usersDao.getByUserName(username);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        // Si es administrador del sistema, tiene visibilidad total
        if ("ROLE_ADMIN".equals(user.getRole())) {
            return reportDao.getAllReports();
        }

        // Obtener los reportes asociados directamente al rol del usuario
        List<Report> reports = new ArrayList<Report>();
        if (user.getRole() != null) {
            for (Report report : reportDao.getReportsByRole(user.getRole())) {
                if (!report.isHidden() && !reports.contains(report)) {
                    reports.add(report);
                }
            }
        }
        return reports;
    }

    public List<ReportGroup> getReportGroupsForUser(String username) {
        return new ArrayList<ReportGroup>();
    }
}
