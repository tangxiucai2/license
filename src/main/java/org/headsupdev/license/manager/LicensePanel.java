/*
 * Copyright 2010-2011 Heads Up Development Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.headsupdev.license.manager;

import org.headsupdev.license.License;
import org.headsupdev.license.LicenseDecoder;
import org.headsupdev.license.LicenseUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.Key;

/**
 * TODO: Document me
 * <p/>
 * Created: 15/03/2012
 *
 * @author Andrew Williams
 * @since 1.1
 */
public class LicensePanel
        extends JPanel {
    private JPanel panel1;
    private JButton newLicenseButton;
    private JList list1;
    private JPanel rightPanel;

    private LicenseManager manager;

    public LicensePanel(final LicenseManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        add(panel1, BorderLayout.CENTER);

        newLicenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                displayAddLicenseForm();
            }
        });

        list1.setListData(manager.getLicenseFiles());
        list1.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
                return super.getListCellRendererComponent(jList, ((File) o).getName(), i, b, b1);
            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                License license = new License();
                File licenseFile = null;
                try {
                    licenseFile = (File) list1.getSelectedValue();
                    Key pub = LicenseUtils.deserialiseKey(manager.getConfig().getPublicKeyFile());
                    Key shr = LicenseUtils.deserialiseKey(manager.getConfig().getSharedKeyFile());

                    LicenseDecoder decoder = new LicenseDecoder();
                    decoder.setPublicKey(pub);
                    decoder.setSharedKey(shr);

                    decoder.decodeLicense(LicenseUtils.readLicenseFile(licenseFile), license);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                displayLicenseDetails(license, licenseFile);
            }
        });
    }

    public void displayLicenseDetails(License license, File file) {
        rightPanel.removeAll();

        rightPanel.add(new LicenseDetailPanel(license, file), BorderLayout.CENTER);
        rightPanel.updateUI();
    }

    public void displayAddLicenseForm() {
        rightPanel.removeAll();

        rightPanel.add(new AddLicenseForm(manager) {
            @Override
            public void licenseAdded(License license, File file) {
                list1.setListData(manager.getLicenseFiles());
                displayLicenseDetails(license, file);
            }
        }, BorderLayout.CENTER);
        rightPanel.updateUI();
    }

    public void displayNoContent() {

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JSplitPane splitPane1 = new JSplitPane();
        panel1.add(splitPane1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        splitPane1.setLeftComponent(panel2);
        newLicenseButton = new JButton();
        newLicenseButton.setText("New License");
        panel2.add(newLicenseButton, BorderLayout.SOUTH);
        list1 = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("license 1");
        defaultListModel1.addElement("license 2");
        list1.setModel(defaultListModel1);
        panel2.add(list1, BorderLayout.CENTER);
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(rightPanel);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(11);
        label1.setText("Choose a license or press the \"New Licese\" button");
        rightPanel.add(label1, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
