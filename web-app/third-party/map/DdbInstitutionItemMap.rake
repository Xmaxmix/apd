# Rakefile - create single combined and compressed javascript file
# for InstitutionItemMap

COMPRESS="java -jar lib/yuicompressor-2.4.2.jar"
COMPRESSED_OUTPUT_FILE='geotemco-min_InstitutionItemMap.js'
OUTPUT_FILE='geotemco_InstitutionItemMap.js'
CSS_FILE='css/geotemco_InstitutionItemMap.css'

task :default =>  [OUTPUT_FILE]

task :all => [COMPRESSED_OUTPUT_FILE, OUTPUT_FILE, CSS_FILE]

# javascript sources
#Files = %w(lib/jquery/jquery.min.js
Files = %w(
    js/Build/Minifier/basic.js
    lib/excanvas/excanvas.js
    lib/slider/js/range.js
    lib/slider/js/slider.js
    lib/slider/js/timer.js
    lib/openlayers/OpenLayers_ddbPatch.js
    js/Util/Tooltips.js
    js/GeoTemConfig.js
    js/Map/MapControl.js
    js/Map/CircleObject.js
    js/Util/FilterBar.js
    js/Util/Selection.js
    js/Map/PlacenameTags.js
    js/Map/MapConfig.js
    js/Map/MapGui.js
    js/Map/MapWidget.js
    js/Util/DataObject.js
    js/Util/Dataset.js
    js/Map/Binning.js
    js/Map/MapDataSource.js
    js/Map/Clustering.js
    js/Util/Dropdown.js
    js/Map/MapZoomSlider.js
    js/Map/MapPopup.js
    js/Map/PlacenamePopup.js
    js/Util/GeoPublisher.js
    js/Util/WidgetWrapper.js
    InstitutionItemMapController.js
    InstitutionItemMapModel.js
    js/Build/Minifier/final.js
)

# css sources
Cssfiles = %w(
    css/style.css
    lib/openlayers/theme/default/style.css
)

def cat_files(outputfile, basename)
  File.open(outputfile, 'w') do |x|
    Files.each do |f|
      x.puts(File.open(f).read.gsub('REPLACEME-REPLACEME', basename))
    end
  end
end

file CSS_FILE => Cssfiles do
  File.open(CSS_FILE, 'w') do |x|
    Cssfiles.each do |f|
      x.puts(File.open(f).read)
    end
  end
end

# Just one big JS file, no compression.
file OUTPUT_FILE => Files do
  basename = File.basename(OUTPUT_FILE, ".js")
  cat_files(OUTPUT_FILE, basename)
end

# Compress it.
file COMPRESSED_OUTPUT_FILE => Files do
  basename = File.basename(COMPRESSED_OUTPUT_FILE, ".js")
  cat_files(OUTPUT_FILE, basename)
  system "#{COMPRESS} #{OUTPUT_FILE} >> #{COMPRESSED_OUTPUT_FILE}"
end

# Clean up the whole thing.
task :clean do
  rm OUTPUT_FILE
  rm COMPRESSED_OUTPUT_FILE
  rm CSS_FILE
end
