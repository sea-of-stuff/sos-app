/*
 * Copyright 2018 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
 *
 * This file is part of the module app.
 *
 * app is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * app is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with app. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.sos.app;

import uk.ac.standrews.cs.castore.CastoreBuilder;
import uk.ac.standrews.cs.castore.CastoreFactory;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IStorage;
import uk.ac.standrews.cs.sos.SettingsConfiguration;
import uk.ac.standrews.cs.sos.exceptions.SOSException;
import uk.ac.standrews.cs.sos.exceptions.storage.DataStorageException;
import uk.ac.standrews.cs.sos.impl.node.LocalStorage;
import uk.ac.standrews.cs.sos.impl.node.SOSLocalNode;

/**
 * The following creates a node instance of the SOS.
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
class ServerState {

    private static SOSLocalNode sos;

    static SOSLocalNode init(SettingsConfiguration.Settings settings) {
        try {
            return ServerState.startSOS(settings);
        } catch (SOSException e) {
            e.printStackTrace();
        }

        return null;
    }

    static void kill() {
        sos.kill(true);
    }

    private static SOSLocalNode startSOS(SettingsConfiguration.Settings settings) throws SOSException {

        LocalStorage localStorage;
        try {
            CastoreBuilder builder = settings.getStore().getCastoreBuilder();
            IStorage storage = CastoreFactory.createStorage(builder);
            localStorage = new LocalStorage(storage);
        } catch (StorageException | DataStorageException e) {
            throw new SOSException(e);
        }

        SOSLocalNode.Builder builder = new SOSLocalNode.Builder();
        sos = builder.settings(settings)
                .internalStorage(localStorage)
                .build();

        return sos;
    }

}
