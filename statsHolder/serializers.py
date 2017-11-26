from statsHolder.models import statsHolder
from rest_framework import serializers

class statsHolderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = statsHolder
        fields = ('id', 'playerName', 'gearNumber', 'twoPointer')
