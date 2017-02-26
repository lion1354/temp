package com.tibco.ma.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import com.tibco.ma.model.File;


@Repository
public class FileDaoImpl extends BaseDaoImpl<File> implements FileDao {

	private static final Logger log = LoggerFactory
			.getLogger(FileDaoImpl.class);

}
