package com.kkrishna.gymtime.dao

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne


@Entity
class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var Id: Long = 0;
	@Expose
	@ManyToOne
	@JoinColumn(name = "email")
	lateinit var client: User;
	@Expose
	lateinit var gymId: String;
	@Expose
	lateinit var trafficMax: String;
	@Expose
	lateinit var startHour: Integer;

	@Expose
	lateinit var endHour: Integer;


	override
	fun toString(): String =
			GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);

}