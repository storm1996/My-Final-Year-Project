# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from rest_framework import viewsets
from rest_framework import permissions
from statsHolder.models import statsHolder
from statsHolder.serializers import statsHolderSerializer

# Create your views here.
class statsHolderViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the Fish table
    queryset = statsHolder.objects.all()
    serializer_class = statsHolderSerializer
