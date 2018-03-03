# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from rest_framework import viewsets
from rest_framework import permissions

from statsHolder.models import statsHolder, User, Team, Fixture, Player, Action, Substitutions
from statsHolder.serializers import statsHolderSerializer, userSerializer, teamSerializer, fixtureSerializer, playerSerializer, actionSerializer, substitutionsSerializer

# Create your views here.
class statsHolderViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the statsHolder table
    queryset = statsHolder.objects.all()
    serializer_class = statsHolderSerializer

class userViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the user table
    queryset = User.objects.all()
    serializer_class = userSerializer

class teamViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the team table
    queryset = Team.objects.all()
    serializer_class = teamSerializer

class fixtureViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the statsHolder table
    queryset = Fixture.objects.all()
    serializer_class = fixtureSerializer

class playerViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the statsHolder table
    queryset = Player.objects.all()
    serializer_class = playerSerializer

class actionViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the statsHolder table
    queryset = Action.objects.all()
    serializer_class = actionSerializer

class substitutionsViewSet(viewsets.ModelViewSet):
    # this fetches all the rows of data in the statsHolder table
    queryset = Substitutions.objects.all()
    serializer_class = substitutionsSerializer
