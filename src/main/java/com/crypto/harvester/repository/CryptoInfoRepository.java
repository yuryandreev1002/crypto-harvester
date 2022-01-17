package com.crypto.harvester.repository;

import org.springframework.data.repository.CrudRepository;

import com.crypto.harvester.model.CryptoInfo;

public interface CryptoInfoRepository extends CrudRepository<CryptoInfo, Long>
{
}
