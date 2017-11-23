
  var PSV;
  var ready = false;
  var markers = new Array();
  var PSVHUD;
  var isGeoEnabled = false;
  var chosenMarkerId = "";
  function initPana(url){
   PSV = new PhotoSphereViewer({
    container: 'photosphere',
    panorama: url,
    anim_speed: '2rpm',
    move_speed: 4.1,
    time_anim: false,
    gyroscope: true,
    webgl: true,
    loading_img:'img/loading.gif',
    usexmpdata:false,
     navbar: [
       'markers',
      'gyroscope',
      'fullscreen'
    ],
    lang:{
    markers: "景点介绍",
    }
    });
    

    PSV.on('panorama-loaded',function(){
 
  
    console.log('sad');
    });
   
    PSV.on('ready',function(){
     ready = true;
       addMarkers();
    });

    PSV.on('click', function(e) {
   chosenMarkerId = "";
  	});
  	
  	 PSV.on('select-marker', function(marker, dblclick) {
    
    chosenMarkerId = marker.id;
  });
    
  }
  
  function clearMakers(){
  markers.splice(0,markers.length);
  PSV.clearMarkers();
  }
  
  
  function swithPana(url){
  if(ready){
    ready = false;
   PSV.setPanorama(url,{longitude: 0,
        latitude: 0},true).then(function(){

   ready = true;
         addMarkers();
   });
 }
  }
  
  function preload(url){
   PSV.preloadPanorama(url);
  }
  
  function isViewReady(){
  return ready;
  }
  
  function addMarker(id,name,imgPath,longtitude,latitude,tooltip){
 markers.push({
       id: id,
        name: name,
        html: imgPath,
        longitude: longtitude,
        latitude: latitude,
        anchor: 'bottom center',
        tooltip:tooltip
      });
  }
  


  
  
  function addMarkers(){
  for(var i=0;i<markers.length;i++)
  {
 PSV.addMarker(markers[i]);
  }}


function resetCurrentMarker(){
chosenMarkerId = -1;
}


  function goToMaker(i){
  PSV.hud.gotoMarker(markers[i],1000);
  }
  function toggleGeo(){
    if(isGeoEnabled){
      PSV.stopGyroscopeControl();
isGeoEnabled = false;
    }else{
      PSV.startGyroscopeControl();
      isGeoEnabled = true;
    }

  }


function getChosenMarkerId(){
console.log(chosenMarkerId);
return chosenMarkerId;
}
  