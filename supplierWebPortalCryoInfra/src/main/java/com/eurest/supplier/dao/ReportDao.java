package com.eurest.supplier.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.*;

@Repository("reportDao")
@Transactional
public class ReportDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    // --- REPORT CRUD ---
    public Report getReportById(int id) {
        return (Report) getSession().get(Report.class, id);
    }

    @SuppressWarnings("unchecked")
    public Report getReportByName(String name) {
        Criteria criteria = getSession().createCriteria(Report.class);
        criteria.add(Restrictions.eq("name", name));
        List<Report> list = criteria.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<Report> getAllReports() {
        Criteria criteria = getSession().createCriteria(Report.class);
        criteria.addOrder(Order.asc("name"));
        return (List<Report>) criteria.list();
    }

    public Report saveReport(Report r) {
        getSession().saveOrUpdate(r);
        return r;
    }

    public void deleteReport(Report r) {
        getSession().delete(r);
    }

    // --- REPORT GROUP CRUD ---
    public ReportGroup getReportGroupById(int id) {
        return (ReportGroup) getSession().get(ReportGroup.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ReportGroup> getAllReportGroups() {
        Criteria criteria = getSession().createCriteria(ReportGroup.class);
        criteria.addOrder(Order.asc("name"));
        return (List<ReportGroup>) criteria.list();
    }

    public ReportGroup saveReportGroup(ReportGroup rg) {
        getSession().saveOrUpdate(rg);
        return rg;
    }

    public void deleteReportGroup(ReportGroup rg) {
        getSession().delete(rg);
    }

    // --- REPORT DATASOURCE CRUD ---
    public ReportDataSource getReportDataSourceById(int id) {
        return (ReportDataSource) getSession().get(ReportDataSource.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ReportDataSource> getAllReportDataSources() {
        Criteria criteria = getSession().createCriteria(ReportDataSource.class);
        criteria.addOrder(Order.asc("name"));
        return (List<ReportDataSource>) criteria.list();
    }

    public ReportDataSource saveReportDataSource(ReportDataSource ds) {
        getSession().saveOrUpdate(ds);
        return ds;
    }

    public void deleteReportDataSource(ReportDataSource ds) {
        getSession().delete(ds);
    }

    // --- REPORT PARAMETER CRUD ---
    public ReportParameter getReportParameterById(int id) {
        return (ReportParameter) getSession().get(ReportParameter.class, id);
    }

    @SuppressWarnings("unchecked")
    public ReportParameter getReportParameterByName(String name) {
        Criteria criteria = getSession().createCriteria(ReportParameter.class);
        criteria.add(Restrictions.eq("name", name));
        List<ReportParameter> list = criteria.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<ReportParameter> getAllReportParameters() {
        Criteria criteria = getSession().createCriteria(ReportParameter.class);
        criteria.addOrder(Order.asc("name"));
        return (List<ReportParameter>) criteria.list();
    }

    public ReportParameter saveReportParameter(ReportParameter p) {
        getSession().saveOrUpdate(p);
        return p;
    }

    public void deleteReportParameter(ReportParameter p) {
        getSession().delete(p);
    }

    // --- REPORT CHART CRUD ---
    public ReportChart getReportChartById(int id) {
        return (ReportChart) getSession().get(ReportChart.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ReportChart> getAllReportCharts() {
        Criteria criteria = getSession().createCriteria(ReportChart.class);
        criteria.addOrder(Order.asc("name"));
        return (List<ReportChart>) criteria.list();
    }

    public ReportChart saveReportChart(ReportChart c) {
        getSession().saveOrUpdate(c);
        return c;
    }

    public void deleteReportChart(ReportChart c) {
        getSession().delete(c);
    }

    // --- DASHBOARD WIDGETS CRUD ---
    @SuppressWarnings("unchecked")
    public List<ReportDashboardWidget> getDashboardWidgetsByGroup(int groupId) {
        Criteria criteria = getSession().createCriteria(ReportDashboardWidget.class);
        criteria.add(Restrictions.eq("groupId", groupId));
        criteria.addOrder(Order.asc("sortOrder"));
        return (List<ReportDashboardWidget>) criteria.list();
    }

    public ReportDashboardWidget saveDashboardWidget(ReportDashboardWidget w) {
        getSession().saveOrUpdate(w);
        return w;
    }

    public void deleteDashboardWidget(ReportDashboardWidget w) {
        getSession().delete(w);
    }

    public void deleteDashboardWidgetsByGroup(int groupId) {
        getSession().createQuery("delete from ReportDashboardWidget where groupId = :groupId")
                   .setParameter("groupId", groupId)
                   .executeUpdate();
    }

    // --- DASHBOARD TABLES CRUD ---
    @SuppressWarnings("unchecked")
    public List<ReportDashboardTable> getDashboardTablesByGroup(int groupId) {
        Criteria criteria = getSession().createCriteria(ReportDashboardTable.class);
        criteria.createAlias("group", "g");
        criteria.add(Restrictions.eq("g.id", groupId));
        criteria.add(Restrictions.eq("isActive", true));
        criteria.addOrder(Order.asc("sortOrder"));
        return (List<ReportDashboardTable>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportDashboardTable> getAllDashboardTables() {
        Criteria criteria = getSession().createCriteria(ReportDashboardTable.class);
        criteria.addOrder(Order.asc("name"));
        return (List<ReportDashboardTable>) criteria.list();
    }

    public ReportDashboardTable saveDashboardTable(ReportDashboardTable t) {
        getSession().saveOrUpdate(t);
        return t;
    }

    public void deleteDashboardTable(ReportDashboardTable t) {
        getSession().delete(t);
    }

    // --- DASHBOARD KPIS CRUD ---
    public ReportKpi getKpiById(int id) {
        return (ReportKpi) getSession().get(ReportKpi.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ReportKpi> getKpisByGroup(int groupId) {
        Criteria criteria = getSession().createCriteria(ReportKpi.class);
        criteria.createAlias("group", "g");
        criteria.add(Restrictions.eq("g.id", groupId));
        criteria.add(Restrictions.eq("isActive", true));
        criteria.addOrder(Order.asc("sortOrder"));
        return (List<ReportKpi>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportKpi> getAllKpis() {
        Criteria criteria = getSession().createCriteria(ReportKpi.class);
        criteria.addOrder(Order.asc("name"));
        return (List<ReportKpi>) criteria.list();
    }

    public ReportKpi saveKpi(ReportKpi kpi) {
        getSession().saveOrUpdate(kpi);
        return kpi;
    }

    public void deleteKpi(ReportKpi kpi) {
        getSession().delete(kpi);
    }

    // --- REPORT LOGS CRUD ---
    public ReportLog saveReportLog(ReportLog log) {
        getSession().saveOrUpdate(log);
        return log;
    }

    @SuppressWarnings("unchecked")
    public List<ReportLog> getReportLogs(int start, int limit) {
        Criteria criteria = getSession().createCriteria(ReportLog.class);
        criteria.addOrder(Order.desc("startTime"));
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        return (List<ReportLog>) criteria.list();
    }

    public int getReportLogsCount() {
        Criteria criteria = getSession().createCriteria(ReportLog.class);
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    // --- ROLE-BASED METHODS ---
    @SuppressWarnings("unchecked")
    public List<Report> getReportsByRole(String role) {
        Criteria criteria = getSession().createCriteria(Report.class);
        criteria.add(Restrictions.like("role", role, org.hibernate.criterion.MatchMode.ANYWHERE));
        criteria.addOrder(Order.asc("name"));
        return (List<Report>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportKpi> getKpisByRole(String role) {
        Criteria criteria = getSession().createCriteria(ReportKpi.class);
        criteria.add(Restrictions.like("role", role, org.hibernate.criterion.MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("isActive", true));
        criteria.addOrder(Order.asc("sortOrder"));
        return (List<ReportKpi>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportChart> getChartsByRole(String role) {
        Criteria criteria = getSession().createCriteria(ReportChart.class);
        criteria.add(Restrictions.like("role", role, org.hibernate.criterion.MatchMode.ANYWHERE));
        criteria.addOrder(Order.asc("name"));
        return (List<ReportChart>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportDashboardTable> getDashboardTablesByRole(String role) {
        Criteria criteria = getSession().createCriteria(ReportDashboardTable.class);
        criteria.add(Restrictions.like("role", role, org.hibernate.criterion.MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("isActive", true));
        criteria.addOrder(Order.asc("sortOrder"));
        return (List<ReportDashboardTable>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportDashboardWidget> getDashboardWidgetsByRole(String role) {
        Criteria criteria = getSession().createCriteria(ReportDashboardWidget.class);
        criteria.add(Restrictions.like("role", role, org.hibernate.criterion.MatchMode.ANYWHERE));
        criteria.addOrder(Order.asc("sortOrder"));
        return (List<ReportDashboardWidget>) criteria.list();
    }

    public void deleteDashboardWidgetsByRole(String role) {
        getSession().createQuery("delete from ReportDashboardWidget where role like :rolePattern")
                   .setParameter("rolePattern", "%" + role + "%")
                   .executeUpdate();
    }
}
