import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Abccovid19TestModule } from '../../../test.module';
import { RevisionDetailComponent } from 'app/entities/revision/revision-detail.component';
import { Revision } from 'app/shared/model/revision.model';

describe('Component Tests', () => {
  describe('Revision Management Detail Component', () => {
    let comp: RevisionDetailComponent;
    let fixture: ComponentFixture<RevisionDetailComponent>;
    const route = ({ data: of({ revision: new Revision(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Abccovid19TestModule],
        declarations: [RevisionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RevisionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RevisionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load revision on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.revision).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
