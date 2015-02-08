package com.util;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;



@Entity
public class ReviewRecord {

	@Column(name = "user_id")
	private int userId;

	@Column(name = "title")
	private String title;

	@Column(name = "assignee_id")
	private int assigneId;

	@Column(name = "created_at")
	private Timestamp created_at;

	@Column(name = "updated_at")
	private Date updated_at;

	@Column(name = "status")
	private String status;

	@Column(name = "severity")
	private String severity;

	@Column(name = "rule_failure_permanent_id")
	private int rule_failure_permanent_id;

	@Column(name = "project_id")
	private int project_id;

	@Column(name = "resource_id")
	private int resource_id;

	@Column(name = "resource_line")
	private Number resource_line;

	@Column(name = "resolution")
	private String resolution;

	@Column(name = "rule_id")
	private int rule_id;

	// @Column(name = "manual_violation")
	private short manual_violation;

	// @Column(name = "manual_severity")
	private short manual_severity;

	@Column(name = "data")
	private String data;

	@Column(name = "DATA_FINAL")
	private String data_final;

	private String author_Name;

	@Column(name = "local_user_id")
	private Number local_User_Id;

	@Column(name = "local_user_name")
	private String local_user_names;

	@Column(name = "long_name")
	private String long_name;
	
	@Column(name = "name")
	private String name;
	
	private String issueKey;

	public String getLong_name() {
		return long_name;
	}

	public void setLong_name(String _long_nameLocal) {
		long_name = _long_nameLocal;
	}

	@Column(name = "id")
	private Number id;

	public String getName() {
		return long_name;
	}

	public void setName(String _nameLocal) {
		long_name = _nameLocal;
	}

	public Number getId() {
		return id;
	}

	public void setId(int _idLocal) {
		id = _idLocal;
	}

	@Column(name = "kee")
	private String project_key;

	public String getProject_key() {
		return project_key;
	}
	
	@Column(name = "login")
	private String login;

	@Column(name = "plugin_rule_key")
	private String plugin_rule_key;
	
	public String getPlugin_rule_key() {
		return plugin_rule_key;
	}

	public void setPlugin_rule_key(String _plugin_rule_keyLocal) {
		plugin_rule_key = _plugin_rule_keyLocal;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String _loginLocal) {
		login = _loginLocal;
	}

	public void setProject_key(String _project_keyLocal) {
		project_key = _project_keyLocal;
	}

	public String getLocal_user_names() {
		return local_user_names;
	}

	public void setLocal_user_names(String _local_user_namesLocal) {
		local_user_names = _local_user_namesLocal;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int _userIdLocal) {
		userId = _userIdLocal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String _titleLocal) {
		title = _titleLocal;
	}

	public int getAssigneId() {
		return assigneId;
	}

	public void setAssigneId(int _assigneIdLocal) {
		assigneId = _assigneIdLocal;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp _created_atLocal) {
		created_at = _created_atLocal;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date _updated_atLocal) {
		updated_at = _updated_atLocal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String _statusLocal) {
		status = _statusLocal;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String _severityLocal) {
		severity = _severityLocal;
	}

	public int getRule_failure_permanent_id() {
		return rule_failure_permanent_id;
	}

	public void setRule_failure_permanent_id(int _rule_failure_permanent_idLocal) {
		rule_failure_permanent_id = _rule_failure_permanent_idLocal;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int _project_idLocal) {
		project_id = _project_idLocal;
	}

	public int getResource_id() {
		return resource_id;
	}

	public void setResource_id(int _resource_idLocal) {
		resource_id = _resource_idLocal;
	}

	public Number getResource_line() {
		return resource_line;
	}

	public void setResource_line(Number _resource_lineLocal) {
		resource_line = _resource_lineLocal;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String _resolutionLocal) {
		resolution = _resolutionLocal;
	}

	public int getRule_id() {
		return rule_id;
	}

	public void setRule_id(int _rule_idLocal) {
		rule_id = _rule_idLocal;
	}

	public short getManual_violation() {
		return manual_violation;
	}

	public void setManual_violation(short _manual_violationLocal) {
		manual_violation = _manual_violationLocal;
	}

	public short getManual_severity() {
		return manual_severity;
	}

	public void setManual_severity(short _manual_severityLocal) {
		manual_severity = _manual_severityLocal;
	}

	public String getData() {
		return data;
	}

	public String getData_final() {
		return data_final;
	}

	public void setData_final(String _data_finalLocal) {
		data_final = _data_finalLocal;
	}

	public void setData(String _dataLocal) {
		data = _dataLocal;
	}

	public String getAuthor_Name() {
		return author_Name;
	}

	public void setAuthor_Name(String _author_NameLocal) {
		author_Name = _author_NameLocal;
	}

	public Number getLocal_User_Id() {
		return local_User_Id;
	}

	public void setLocal_User_Id(Number _local_User_IdLocal) {
		local_User_Id = _local_User_IdLocal;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public String getIssueKey() {
		return issueKey;
	}

}
