# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
class statsHolder(models.Model):
	playerName = models.CharField(max_length=255)
	gearNumber = models.IntegerField()
	twoPointer = models.IntegerField()

