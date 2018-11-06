package eyja.lab.tools.cell.counter.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import eyja.lab.tools.cell.counter.functionality.CellCountResourceType;
import eyja.lab.tools.cell.counter.functionality.Count;
import eyja.lab.tools.cell.counter.functionality.CountedDilution;
import eyja.lab.tools.cell.counter.functionality.Dilution;
import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.OriginDeserialiser;
import eyja.lab.tools.control.centre.management.OriginHandler;
import eyja.lab.tools.control.centre.management.Project;
import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.operation.Initialiser;

public class CellCountInitialiser implements Initialiser, OriginDeserialiser {
	
	private static CellCountInitialiser mainInit = new CellCountInitialiser();
	private static final File DEFAULT_ORIGIN_LOCATION = new File("CellCount" + Origin.ORIGIN_EXTENSION);	
	private static Origin countOrigin = null;
	private static OriginHandler mainHandler = null;

	public static void main(String[] args) {
		// TODO: standalone initialisation
	}
	
	/**
	 * Get the main origin managing resources of the cell count module.
	 * 
	 * @return the main origin
	 */
	public static Origin getMainOrigin() {
		return CellCountInitialiser.countOrigin;
	}
	
	public static OriginHandler getMainHandler() {
		return CellCountInitialiser.mainHandler;
	}

	@Override
	public void initialise(Project project) {
		if (project != null) {
			CellCountInitialiser.mainHandler = project.getOriginHandler();
			CellCountInitialiser.countOrigin = new Origin(project.getProjectFolder().toPath().resolve(
					CellCountInitialiser.DEFAULT_ORIGIN_LOCATION.toPath()).toFile(), 
					CellCountInitialiser.mainInit);
			CellCountInitialiser.mainHandler.requestAdd(CellCountInitialiser.countOrigin);
		} else { // default initialisation
			CellCountInitialiser.countOrigin = new Origin(CellCountInitialiser.DEFAULT_ORIGIN_LOCATION, 
					CellCountInitialiser.mainInit);
			CellCountInitialiser.mainHandler = new OriginHandler();
			CellCountInitialiser.mainHandler.requestAdd(CellCountInitialiser.countOrigin);
		}
	}

	@Override
	public void deserialise(InputStream originData, Origin originToBuild) throws IOException {
		if (originToBuild != null && originData != null) {
			try (DataInputStream dis = new DataInputStream(originData)) {
				originToBuild.clear(); // ensure the origin is empty before building it
				while (dis.available() > 0) {
					CellCountResourceType type = CellCountResourceType.fromByte(dis.readByte());
					ResourceID id = new ResourceID(originToBuild, dis.readLong());
					Resource builtResource = null;
					switch (type) {
					
					case COUNT:
						byte[] binaryCountLDT = new byte[BinaryConverter.LOCAL_DATE_TIME_BYTES];
						dis.read(binaryCountLDT);
						if (dis.readBoolean()) {
							builtResource = new Count(dis.readInt(), dis.readInt(), dis.readDouble(), 
									BinaryConverter.getLocalDateTime(binaryCountLDT));
						} else {
							builtResource = new Count(dis.readDouble(), BinaryConverter.getLocalDateTime(binaryCountLDT));
						}
						break;
						
					case DILUTION:
						byte[] binaryDilutionLDT = new byte[BinaryConverter.LOCAL_DATE_TIME_BYTES];
						dis.read(binaryDilutionLDT);
						builtResource = new Dilution(dis.readDouble(), dis.readDouble(), 
								BinaryConverter.getLocalDateTime(binaryDilutionLDT));
						break;
						
					case COUNTED_DILUTION:
						int countLength = dis.readInt();
						byte[] binaryCountRef = new byte[countLength];
						dis.read(binaryCountRef);
						int dilutionLength = dis.readInt();
						byte[] binaryDilutionRef = new byte[dilutionLength];
						dis.read(binaryDilutionRef);
						builtResource = new CountedDilution(BinaryConverter.getResourceReference(binaryCountRef), 
								BinaryConverter.getResourceReference(binaryDilutionRef));
						break;
						
					case COUNTING_SERIES:
						// TODO: implement
						break;
					
					default:
						throw new IOException(String.format("The resource type %s does not have a "
								+ "deserialisation implementation.", type));
					
					}
					builtResource.setID(id);
					originToBuild.requestAdd(builtResource);
				}
			}
		} else {
			throw new NullPointerException(String.format("Cannot deserialise with input"
					+ " %s and target origin %s.", originData, originToBuild));
		}
	}

}
