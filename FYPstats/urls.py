"""FYPstats URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.contrib import admin
from rest_framework import routers
from statsHolder import views
from django.conf.urls import url, include

router = routers.DefaultRouter()
#makes sure that the API endpoints work
router.register(r'api/statsHolder', views.statsHolderViewSet)
router.register(r'api/User', views.userViewSet)
router.register(r'api/team', views.teamViewSet)
router.register(r'api/fixture', views.fixtureViewSet)
router.register(r'api/player', views.playerViewSet)
router.register(r'api/action', views.actionViewSet)
router.register(r'api/substitutions', views.substitutionsViewSet)
admin.autodiscover()

urlpatterns = [
    # Examples:
    # url(r'^$', 'marine.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]
