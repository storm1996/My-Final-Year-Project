# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
class statsHolder(models.Model):
	playerName = models.CharField(max_length=255)
	gearNumber = models.IntegerField()
	twoPointer = models.IntegerField()

class User(models.Model):
	user_id = models.AutoField(primary_key=True)
	username = models.CharField(max_length=15, unique=True)
	password = models.CharField(max_length=25, unique=True)

class Team(models.Model):
	team_id = models.AutoField(primary_key=True)
	team_name = models.CharField(max_length=25)
	league = models.CharField(max_length=25)
	home_location = models.CharField(max_length=55)
	user_id =

class Fixture(models.Model):
	fixture_id = models.AutoField(primary_key=True)
	home_team =
	away_team =
	fixture_date = models.DateField()
	venue = models.CharField(max_length=55)

class Player(models.Model):
	player_id = models.AutoField(primary_key=True)
	player_name = models.CharField(max_length=25)
	player_number = models.IntegerField()
	team_id =

class Action(models.Model):
	action_id = models.AutoField(primary_key=True)
	action_type = models.CharField(max_length=25)
	action_timestamp = models.DateTimeField(auto_now_add=True)
	player_id =
	fixture_id =

class Substitutions(models.Model):
	sub_id = models.AutoField(primary_key=True)
	sub_timestamp = models.DateTimeField(auto_now_add=True)
	sub_in = models.BooleanField()
	sub_out = models.BooleanField()
	player_id =
	fixture_id =
