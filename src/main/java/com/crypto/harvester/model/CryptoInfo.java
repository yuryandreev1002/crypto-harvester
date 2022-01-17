package com.crypto.harvester.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "crypto_info")
public class CryptoInfo
{
	@Id
	@Column(name = "id", nullable = false, unique = true)
	@GeneratedValue
	private UUID id;

	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "time", nullable = false)
	private LocalDateTime time;

	@Column(name = "bid", nullable = false)
	private BigDecimal bid;

	@Column(name = "ask", nullable = false)
	private BigDecimal ask;

	@Column(name = "exchange", nullable = false)
	private String exchange;

	@Column(name = "name", nullable = false)
	private String name;
}
